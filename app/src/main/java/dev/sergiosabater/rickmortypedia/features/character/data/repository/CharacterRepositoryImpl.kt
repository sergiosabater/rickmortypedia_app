package dev.sergiosabater.rickmortypedia.features.character.data.repository

import android.util.Log
import dev.sergiosabater.rickmortypedia.core.common.error.DomainError
import dev.sergiosabater.rickmortypedia.core.network.RickAndMortyApiService
import dev.sergiosabater.rickmortypedia.features.character.data.local.dao.CharacterDao
import dev.sergiosabater.rickmortypedia.features.character.data.local.dao.PaginationInfoDao
import dev.sergiosabater.rickmortypedia.features.character.data.local.entity.PaginationInfoEntity
import dev.sergiosabater.rickmortypedia.features.character.data.local.mapper.CharacterEntityMapper
import dev.sergiosabater.rickmortypedia.features.character.data.remote.CharacterMapper
import dev.sergiosabater.rickmortypedia.features.character.data.remote.PageInfoDto
import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.repository.CharacterRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CharacterRepositoryImpl(
    private val apiService: RickAndMortyApiService,
    private val mapper: CharacterMapper,
    private val characterDao: CharacterDao,
    private val paginationInfoDao: PaginationInfoDao,
    private val entityMapper: CharacterEntityMapper
) : CharacterRepository {

    override suspend fun getCharacters(page: Int?): Result<List<Character>> {
        return try {
            if (page != null) {
                // Load specific page
                if (hasCachedData()) {
                    val localCharacters = getCharactersFromLocal(page)
                    if (localCharacters.isNotEmpty()) {
                        Result.success(localCharacters)
                    } else {
                        Result.failure(DomainError.NoCachedData())
                    }
                } else {
                    syncWithApi(page)
                    val refreshedCharacters = getCharactersFromLocal(page)
                    if (refreshedCharacters.isNotEmpty()) {
                        Result.success(refreshedCharacters)
                    } else {
                        Result.failure(DomainError.NoCharactersFound())
                    }
                }
            } else {
                // Load all pages
                if (hasCachedData()) {
                    val localCharacters = getCharactersFromLocal(null)
                    if (localCharacters.isNotEmpty()) {
                        return Result.success(localCharacters)
                    }
                }

                // Synchronize all pages
                var totalPages = paginationInfoDao.getPaginationInfo()?.totalPages
                if (totalPages == null) {
                    // Synchronize first page to get totalPages
                    syncWithApi(1)
                    totalPages = paginationInfoDao.getPaginationInfo()?.totalPages ?: 1
                }

                var successCount = 0
                for (currentPage in 1..totalPages) {
                    try {
                        syncWithApi(currentPage)
                        successCount++
                    } catch (e: Exception) {
                        Log.e("Repository", "Error syncing page $currentPage: ${e.message}")
                        // Continue with next pages
                    }
                }

                if (successCount == 0) {
                    return Result.failure(DomainError.SyncFailed())
                }

                // Get all characters from cache
                val allCharacters = getCharactersFromLocal(null)
                if (allCharacters.isNotEmpty()) {
                    Result.success(allCharacters)
                } else {
                    Result.failure(DomainError.NoCharactersFound())
                }
            }
        } catch (e: DomainError) {
            Result.failure(e)
        } catch (_: Exception) {
            Result.failure(DomainError.DatabaseError())
        }
    }

    override suspend fun getCharacterById(id: Int): Result<Character> {
        return try {
            // 1. First check if data in the cache
            val localCharacter = getCharacterFromLocal(id)
            if (localCharacter != null) {
                Result.success(localCharacter)
            } else {
                // 2. If no cache, connecting with API
                val apiResult = safeApiCall {
                    val dto = apiService.getCharacterById(id)
                    mapper.toCharacter(dto)
                }

                if (apiResult.isSuccess) {
                    val character = apiResult.getOrNull()
                    if (character != null) {
                        // 3. Save in cache for future references
                        saveCharacterToLocal(character)
                        Result.success(character)
                    } else {
                        Result.failure(DomainError.CharacterNotFound(id))
                    }
                } else {
                    val exception = apiResult.exceptionOrNull()
                    Result.failure(
                        exception as? DomainError ?: DomainError.UnknownError()
                    )
                }
            }
        } catch (_: Exception) {
            Result.failure(DomainError.DatabaseError())
        }
    }

    override suspend fun searchCharacters(
        name: String?,
        status: String?,
        species: String?
    ): Result<List<Character>> {
        return try {
            // Always search local cache for searches
            if (!hasCachedData()) {
                return Result.failure(DomainError.NoCachedData())
            }

            val searchResults = when {
                !name.isNullOrBlank() -> {
                    characterDao.searchCharacters(name).map { entityMapper.toDomain(it) }
                }

                else -> {
                    // Only search by name
                    // In the future is possible advanced search as well
                    getCharactersFromLocal()
                }
            }

            if (searchResults.isNotEmpty()) {
                Result.success(searchResults)
            } else {
                Result.failure(DomainError.NoCharactersFound())
            }
        } catch (_: Exception) {
            Result.failure(DomainError.DatabaseError())
        }
    }

    override suspend fun hasCachedData(): Boolean {
        return try {
            characterDao.getCharacterCount() > 0
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun clearCache(): Result<Boolean> {
        return try {
            characterDao.deleteAllCharacters()
            paginationInfoDao.deletePaginationInfo()
            Result.success(true)
        } catch (_: Exception) {
            Result.failure(DomainError.DatabaseError())
        }
    }

    private suspend inline fun <T> safeApiCall(
        characterId: Int? = null,
        apiCall: suspend () -> T
    ): Result<T> {
        return try {
            val result = apiCall()
            Result.success(result)
        } catch (domainError: DomainError) {
            Result.failure(domainError)
        } catch (e: ClientRequestException) {
            val statusCode = e.response.status.value
            val error = when (statusCode) {
                404 -> characterId?.let { DomainError.CharacterNotFound(it) }
                    ?: DomainError.NoCharactersFound()

                400 -> DomainError.NetworkError()  // Bad Request
                401, 403 -> DomainError.NetworkError()  // Unauthorized/Forbidden
                else -> DomainError.NetworkError()
            }
            Result.failure(error)
        } catch (_: ServerResponseException) {
            Result.failure(DomainError.ServerError())
        } catch (_: UnknownHostException) {
            Result.failure(DomainError.NetworkError())
        } catch (_: SocketTimeoutException) {
            Result.failure(DomainError.NetworkError())
        } catch (_: IOException) {
            Result.failure(DomainError.NetworkError())
        } catch (e: Exception) {
            Log.e("Repository", "Unexpected error: ${e.message}", e)
            Result.failure(DomainError.UnknownError())
        }
    }

    private suspend inline fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return safeApiCall(null, apiCall)
    }

    private suspend fun getCharactersFromLocal(page: Int? = null): List<Character> {
        return try {
            val entities = if (page != null) {
                characterDao.getCharactersByPage(page)
            } else {
                characterDao.getCharacters(Int.MAX_VALUE, 0)
            }
            entities.map { entityMapper.toDomain(it) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun getCharacterFromLocal(id: Int): Character? {
        return try {
            val entity = characterDao.getCharacterById(id)
            entity?.let { entityMapper.toDomain(it) }
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun saveCharactersToLocal(
        characters: List<Character>,
        page: Int = 1,
        pageInfo: PageInfoDto? = null
    ) {
        try {
            val entities = characters.map { entityMapper.toEntity(it, page) }
            characterDao.insertCharacters(entities)

            pageInfo?.let { info ->
                val paginationInfo = PaginationInfoEntity(
                    id = 1,
                    totalPages = info.pages,
                    totalCharacters = info.count,
                    lastSyncTimestamp = System.currentTimeMillis()
                )
                paginationInfoDao.insertPaginationInfo(paginationInfo)
            }
        } catch (_: Exception) {
            throw DomainError.DatabaseError()
        }
    }

    private suspend fun saveCharacterToLocal(character: Character) {
        try {
            val entity = entityMapper.toEntity(character, 1)
            characterDao.insertCharacter(entity)
        } catch (_: Exception) {
            throw DomainError.DatabaseError()
        }
    }

    private suspend fun syncWithApi(page: Int = 1) {
        val apiResult = safeApiCall {
            val response = apiService.getCharacters(page = page)
            response to response.results.map { dto -> mapper.toCharacter(dto) }
        }

        when {
            apiResult.isSuccess -> {
                val (apiResponse, characters) = apiResult.getOrNull()!!
                if (characters.isNotEmpty()) {
                    saveCharactersToLocal(characters, page, apiResponse.info)
                } else {
                    throw DomainError.NoCharactersFound()
                }
            }

            else -> {
                val originalError =
                    apiResult.exceptionOrNull() as? DomainError ?: DomainError.SyncFailed()
                throw originalError
            }
        }
    }
}