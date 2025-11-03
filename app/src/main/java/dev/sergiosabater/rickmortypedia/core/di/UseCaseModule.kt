package dev.sergiosabater.rickmortypedia.core.di

import dev.sergiosabater.rickmortypedia.features.character.domain.usecase.GetCharacterByIdUseCase
import dev.sergiosabater.rickmortypedia.features.character.domain.usecase.GetCharactersUseCase
import dev.sergiosabater.rickmortypedia.features.character.domain.usecase.SearchCharactersUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {

    factoryOf(::GetCharactersUseCase)
    factoryOf(::GetCharacterByIdUseCase)
    factoryOf(::SearchCharactersUseCase)

}