package dev.sergiosabater.rickmortypedia.features.character.domain.repository

import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character

interface CharacterRepository {
    suspend fun getCharacters(page: Int? = null): Result<List<Character>>
    suspend fun getCharacterById(id: Int): Result<Character>
    suspend fun searchCharacters(
        name: String? = null,
        status: String? = null,
        species: String? = null
    ): Result<List<Character>>

    suspend fun hasCachedData(): Boolean
    suspend fun clearCache(): Result<Boolean>
}