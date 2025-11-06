package dev.sergiosabater.rickmortypedia.features.character.domain.model

import kotlin.text.lowercase

data class Character(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: String,
    val origin: String,
    val location: String,
    val image: String,
    val episodeCount: Int,
    val originUrl: String = "",
    val locationUrl: String = "",
    val episodeUrls: List<String> = emptyList(),
    val url: String = "",
    val created: String = ""
)

enum class CharacterStatus {
    ALIVE, DEAD, UNKNOWN;

    companion object {
        fun fromString(status: String): CharacterStatus {
            return when (status.lowercase()) {
                "alive" -> ALIVE
                "dead" -> DEAD
                else -> UNKNOWN
            }
        }
    }
}