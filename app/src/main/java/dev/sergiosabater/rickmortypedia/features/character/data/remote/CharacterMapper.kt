package dev.sergiosabater.rickmortypedia.features.character.data.remote

import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.model.CharacterStatus

class CharacterMapper() {
    fun toCharacter(dto: CharacterDto): Character {
        return Character(
            id = dto.id,
            name = dto.name,
            status = CharacterStatus.Companion.fromString(dto.status),
            species = dto.species,
            type = dto.type.ifEmpty { "Unknown" },
            gender = dto.gender,
            origin = dto.origin.name,
            location = dto.location.name,
            image = dto.image,
            episodeCount = dto.episode.size,
            originUrl = dto.origin.url,
            locationUrl = dto.location.url,
            episodeUrls = dto.episode,
            url = dto.url,
            created = dto.created
        )
    }
}