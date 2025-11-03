package dev.sergiosabater.rickmortypedia.core.di

import androidx.room.Room
import dev.sergiosabater.rickmortypedia.core.data.database.AppDatabase
import dev.sergiosabater.rickmortypedia.features.character.data.local.CharacterDao
import dev.sergiosabater.rickmortypedia.features.character.data.local.PaginationInfoDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    single<CharacterDao> {
        get<AppDatabase>().characterDao()
    }

    single<PaginationInfoDao> {
        get<AppDatabase>().paginationInfoDao()
    }
}