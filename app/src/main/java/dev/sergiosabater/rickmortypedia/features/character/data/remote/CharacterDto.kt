package dev.sergiosabater.rickmortypedia.features.character.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("status")
    val status: String,

    @SerialName("species")
    val species: String,

    @SerialName("type")
    val type: String,

    @SerialName("gender")
    val gender: String,

    @SerialName("origin")
    val origin: CharacterLocationDto,

    @SerialName("location")
    val location: CharacterLocationDto,

    @SerialName("image")
    val image: String,

    @SerialName("episode")
    val episode: List<String>,

    @SerialName("url")
    val url: String,

    @SerialName("created")
    val created: String
)

@Serializable
data class CharacterLocationDto(
    @SerialName("name")
    val name: String,

    @SerialName("url")
    val url: String
)

@Serializable
data class CharactersResponseDto(
    @SerialName("info")
    val info: PageInfoDto,

    @SerialName("results")
    val results: List<CharacterDto>
)

@Serializable
data class PageInfoDto(
    @SerialName("count")
    val count: Int,

    @SerialName("pages")
    val pages: Int,

    @SerialName("next")
    val next: String?,

    @SerialName("prev")
    val prev: String?
)