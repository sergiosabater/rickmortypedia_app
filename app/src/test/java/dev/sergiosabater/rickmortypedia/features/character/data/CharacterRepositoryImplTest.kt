package dev.sergiosabater.rickmortypedia.features.character.data

import dev.sergiosabater.rickmortypedia.core.network.RickAndMortyApiService
import dev.sergiosabater.rickmortypedia.features.character.data.local.dao.CharacterDao
import dev.sergiosabater.rickmortypedia.features.character.data.local.entity.CharacterEntity
import dev.sergiosabater.rickmortypedia.features.character.data.local.mapper.CharacterEntityMapper
import dev.sergiosabater.rickmortypedia.features.character.data.local.dao.PaginationInfoDao
import dev.sergiosabater.rickmortypedia.features.character.data.remote.CharacterDto
import dev.sergiosabater.rickmortypedia.features.character.data.remote.CharacterMapper
import dev.sergiosabater.rickmortypedia.features.character.data.remote.CharactersResponseDto
import dev.sergiosabater.rickmortypedia.features.character.data.remote.PageInfoDto
import dev.sergiosabater.rickmortypedia.features.character.data.repository.CharacterRepositoryImpl
import dev.sergiosabater.rickmortypedia.util.ViewModelTest
import dev.sergiosabater.rickmortypedia.util.fixtures.CharacterTestFixtures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CharacterRepositoryImplTest : ViewModelTest() {

    private lateinit var repository: CharacterRepositoryImpl
    private lateinit var apiService: RickAndMortyApiService
    private lateinit var mapper: CharacterMapper
    private lateinit var characterDao: CharacterDao
    private lateinit var paginationInfoDao: PaginationInfoDao
    private lateinit var entityMapper: CharacterEntityMapper

    @Before
    fun setUp() {
        // Initialize all dependency mocks
        apiService = mock()
        mapper = mock()
        characterDao = mock()
        paginationInfoDao = mock()
        entityMapper = mock()

        // Create repository instance with mocked dependencies
        repository = CharacterRepositoryImpl(
            apiService = apiService,
            mapper = mapper,
            characterDao = characterDao,
            paginationInfoDao = paginationInfoDao,
            entityMapper = entityMapper
        )
    }

    // Verifies that when there is cached data, the repository uses the local database and does NOT call the API.
    @Test
    fun `getCharacters with cached data should return local characters`() = runTest {
        // Given - Scenario setup: there is cached data
        val page = 1
        val expectedCharacters = CharacterTestFixtures.mockCharacters

        // Create entity mocks and configure mapper to convert Entity -> Domain
        val characterEntities = expectedCharacters.map {
            mock<CharacterEntity>().apply {
                // Configure that when toDomain is called with this entity, it returns the corresponding character
                whenever(entityMapper.toDomain(this)).thenReturn(it)
            }
        }

        // Configure mocks. There is data in local database
        whenever(characterDao.getCharacterCount()).thenReturn(4) // There are 4 characters in cache
        whenever(characterDao.getCharactersByPage(page)).thenReturn(characterEntities) // Return the mocked entities

        // When - Execute the method under test
        val result = repository.getCharacters(page)

        // Then - Verify expected behavior
        assertTrue(result.isSuccess) // Should be a successful result
        assertEquals(expectedCharacters, result.getOrNull()) // Characters should match expected ones
        verify(characterDao).getCharactersByPage(page) // Should have queried local database
        verifyNoInteractions(apiService) // Should NOT have called the API (because there's cache)
    }


    /*  Verifies that when there's NO cached data the repository:
        - Calls the API to synchronize
        - Saves data to the local database
        - Returns the newly synchronized data
    */
    @Test
    fun `getCharacters without cached data should sync with API and return characters`() = runTest {
        // Given - Scenario setup: NO cached data
        val page = 1
        val apiResponse = mock<CharactersResponseDto>() // Mock API response
        val pageInfo = mock<PageInfoDto>() // Mock pagination info
        val characterDtos = listOf(mock<CharacterDto>()) // Mock API DTOs
        val expectedCharacters = CharacterTestFixtures.mockCharacters
        val characterEntities = expectedCharacters.map { mock<CharacterEntity>() } // Entity mocks

        // Configure mocks: initially empty database
        whenever(characterDao.getCharacterCount()).thenReturn(0) // No cached data

        // Configure API calls
        whenever(apiService.getCharacters(page)).thenReturn(apiResponse) // API returns mocked response
        whenever(apiResponse.results).thenReturn(characterDtos) // Response contains DTOs
        whenever(apiResponse.info).thenReturn(pageInfo) // Response contains pagination info

        // Configure mappers: DTO -> Domain and Entity -> Domain
        whenever(mapper.toCharacter(any<CharacterDto>())).thenReturn(expectedCharacters[0])
        whenever(characterDao.getCharactersByPage(page)).thenReturn(characterEntities) // After sync, DB has data
        whenever(entityMapper.toDomain(any<CharacterEntity>())).thenReturn(expectedCharacters[0])

        // When - Execute the method under test
        val result = repository.getCharacters(page)

        // Then - Verify expected behavior
        assertTrue(result.isSuccess) // Should be a successful result
        assertEquals(expectedCharacters.size, result.getOrNull()?.size) // Should return expected number of characters

        // Verify interactions: should have called API and database
        verify(apiService).getCharacters(page) // Called API for synchronization
        verify(characterDao).getCharactersByPage(page) // Queried local database
        verify(characterDao).insertCharacters(any()) // Saved characters to database
        verify(paginationInfoDao).insertPaginationInfo(any()) // Saved pagination info
    }

    //  Verifies that when searching for a character by ID that is cached, it uses the local database and does NOT call the API.
    @Test
    fun `getCharacterById with cached data should return local character`() = runTest {
        // Given - Scenario setup: character is cached
        val characterId = 1
        val expectedCharacter = CharacterTestFixtures.character1
        val characterEntity = mock<CharacterEntity>() // Mock entity from database

        // Configure mocks: character exists in local database
        whenever(characterDao.getCharacterById(characterId)).thenReturn(characterEntity)
        whenever(entityMapper.toDomain(characterEntity)).thenReturn(expectedCharacter) // Entity -> Domain

        // When - Execute the method under test
        val result = repository.getCharacterById(characterId)

        // Then - Verify expected behavior
        assertTrue(result.isSuccess) // Should be a successful result
        assertEquals(expectedCharacter, result.getOrNull()) // Character should match expected one

        // Verify interactions
        verify(characterDao).getCharacterById(characterId) // Queried local database
        verifyNoInteractions(apiService) // Did NOT call API (because it was cached)
    }


    // Verifies that when searching for a character by ID that is NOT cached
    @Test
    fun `getCharacterById without cached data should fetch from API and save to local`() = runTest {
        // Given - Scenario setup: character is NOT cached
        val characterId = 1
        val expectedCharacter = CharacterTestFixtures.character1
        val characterDto = mock<CharacterDto>() // Mock API DTO
        val characterEntity = mock<CharacterEntity>() // Mock entity to save to DB

        // Configure mocks: character NOT found in local database
        whenever(characterDao.getCharacterById(characterId)).thenReturn(null) // Not found in cache

        // Configure API calls and mappers
        whenever(apiService.getCharacterById(characterId)).thenReturn(characterDto) // API returns DTO
        whenever(mapper.toCharacter(characterDto)).thenReturn(expectedCharacter) // DTO -> Domain
        whenever(entityMapper.toEntity(expectedCharacter, 1)).thenReturn(characterEntity) // Domain -> Entity

        // When - Execute the method under test
        val result = repository.getCharacterById(characterId)

        // Then - Verify expected behavior
        assertTrue(result.isSuccess) // Should be a successful result
        assertEquals(expectedCharacter, result.getOrNull()) // Character should match expected one

        // Verify interactions: called API and saved to database
        verify(apiService).getCharacterById(characterId) // Called API to get character
        verify(characterDao).insertCharacter(characterEntity) // Saved character to local database
    }
}