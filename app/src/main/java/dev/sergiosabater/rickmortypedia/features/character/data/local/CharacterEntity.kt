package dev.sergiosabater.rickmortypedia.features.character.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val originUrl: String,
    val locationName: String,
    val locationUrl: String,
    val image: String,
    val episodeUrls: String,
    val url: String,
    val created: String,

    // Additional fields for local management
    val lastUpdated: Long = System.currentTimeMillis(),
    val page: Int = 1  // For local pagination
)