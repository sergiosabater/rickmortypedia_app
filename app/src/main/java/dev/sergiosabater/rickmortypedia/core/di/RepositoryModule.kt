package dev.sergiosabater.rickmortypedia.core.di

import dev.sergiosabater.rickmortypedia.features.character.data.CharacterRepositoryImpl
import dev.sergiosabater.rickmortypedia.features.character.data.local.CharacterEntityMapper
import dev.sergiosabater.rickmortypedia.features.character.data.remote.CharacterMapper
import dev.sergiosabater.rickmortypedia.features.character.domain.repository.CharacterRepository
import org.koin.dsl.module

val repositoryModule = module {

    // Mappers
    single { CharacterMapper() }
    single { CharacterEntityMapper() }

    // Repository
    single<CharacterRepository> {
        CharacterRepositoryImpl(
            apiService = get(),
            mapper = get(),
            characterDao = get(),
            paginationInfoDao = get(),
            entityMapper = get()
        )
    }
}