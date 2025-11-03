package dev.sergiosabater.rickmortypedia.features.character.domain.usecase

import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.repository.CharacterRepository

class SearchCharactersUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(
        name: String? = null,
        status: String? = null,
        species: String? = null
    ): Result<List<Character>> {
        return repository.searchCharacters(name, status, species)
    }
}