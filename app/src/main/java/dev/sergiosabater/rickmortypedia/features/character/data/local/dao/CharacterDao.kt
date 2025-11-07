package dev.sergiosabater.rickmortypedia.features.character.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.sergiosabater.rickmortypedia.features.character.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    // Insert or replace characters (upsert)
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    // Insert or replace a single character
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    // Get all paginated characters
    @Query("SELECT * FROM characters ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getCharacters(limit: Int, offset: Int): List<CharacterEntity>

    // Get character by ID
    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    // Search for characters by name (case-insensitive search)
    @Query("SELECT * FROM characters WHERE name LIKE '%' || :name || '%'")
    suspend fun searchCharacters(name: String): List<CharacterEntity>

    // Get characters per page (for local pagination)
    @Query("SELECT * FROM characters WHERE page = :page ORDER BY id ASC")
    suspend fun getCharactersByPage(page: Int): List<CharacterEntity>

    // Get the total number of characters
    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharacterCount(): Int

    // Delete all characters
    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()

    // Remove characters from a specific page (for reloading)
    @Query("DELETE FROM characters WHERE page = :page")
    suspend fun deleteCharactersByPage(page: Int)

    // Get the last loaded page
    @Query("SELECT MAX(page) FROM characters")
    suspend fun getLastLoadedPage(): Int?
}