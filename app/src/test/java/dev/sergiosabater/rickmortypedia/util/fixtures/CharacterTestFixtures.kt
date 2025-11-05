package dev.sergiosabater.rickmortypedia.util.fixtures

import dev.sergiosabater.rickmortypedia.features.character.domain.model.Character
import dev.sergiosabater.rickmortypedia.features.character.domain.model.CharacterStatus

object CharacterTestFixtures {

    val character1 = Character(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.ALIVE,
        species = "Human",
        type = "",
        gender = "Male",
        origin = "Earth",
        location = "Earth",
        image = "https://example.com/rick.png",
        episodeCount = 51
    )

    val character2 = Character(
        id = 2,
        name = "Morty Smith",
        status = CharacterStatus.ALIVE,
        species = "Human",
        type = "",
        gender = "Male",
        origin = "Earth",
        location = "Earth",
        image = "https://example.com/morty.png",
        episodeCount = 51
    )

    val character3 = Character(
        id = 3,
        name = "Summer Smith",
        status = CharacterStatus.ALIVE,
        species = "Human",
        type = "",
        gender = "Female",
        origin = "Earth",
        location = "Earth",
        image = "https://example.com/summer.png",
        episodeCount = 42
    )

    val character4 = Character(
        id = 4,
        name = "Birdperson",
        status = CharacterStatus.ALIVE,
        species = "Alien",
        type = "Bird-Person",
        gender = "Male",
        origin = "Bird World",
        location = "Bird World",
        image = "https://example.com/birdperson.png",
        episodeCount = 10
    )

    val mockCharacters = listOf(
        character1,
        character2,
        character3,
        character4
    )
}