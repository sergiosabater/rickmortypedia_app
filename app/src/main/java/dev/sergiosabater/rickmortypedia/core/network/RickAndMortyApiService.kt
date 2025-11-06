package dev.sergiosabater.rickmortypedia.core.network

import dev.sergiosabater.rickmortypedia.features.character.data.remote.CharacterDto
import dev.sergiosabater.rickmortypedia.features.character.data.remote.CharactersResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RickAndMortyApiService(
    private val httpClient: HttpClient
) {
    suspend fun getCharacters(
        page: Int? = null,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ): CharactersResponseDto {
        return httpClient.get(CHARACTER_LIST) {
            page?.let { parameter(QUERY_PAGE, it) }
            name?.let { parameter(QUERY_NAME, it) }
            status?.let { parameter(QUERY_STATUS, it) }
            species?.let { parameter(QUERY_SPECIES, it) }
            type?.let { parameter(QUERY_TYPE, it) }
            gender?.let { parameter(QUERY_GENDER, it) }
        }.body()
    }

    suspend fun getCharacterById(id: Int): CharacterDto {
        val url = CHARACTER_BY_ID.replace("{id}", id.toString())

        return httpClient.get(url).body()
    }
}