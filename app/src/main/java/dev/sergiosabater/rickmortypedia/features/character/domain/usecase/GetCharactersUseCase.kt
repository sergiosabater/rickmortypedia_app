package dev.sergiosabater.rickmortypedia.features.character.domain.usecase

import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.repository.CharacterRepository

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int? = null): Result<List<Character>> {
        return repository.getCharacters(page)
    }
}