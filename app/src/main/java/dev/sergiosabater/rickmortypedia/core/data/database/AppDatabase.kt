package dev.sergiosabater.rickmortypedia.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.sergiosabater.rickmortypedia.features.character.data.local.CharacterDao
import dev.sergiosabater.rickmortypedia.features.character.data.local.PaginationInfoDao
import dev.sergiosabater.rickmortypedia.features.character.data.local.CharacterEntity
import dev.sergiosabater.rickmortypedia.features.character.data.local.PaginationInfoEntity

@Database(
    entities = [
        CharacterEntity::class,
        PaginationInfoEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun paginationInfoDao(): PaginationInfoDao

    companion object {
        const val DATABASE_NAME = "rick_and_morty.db"
    }
}