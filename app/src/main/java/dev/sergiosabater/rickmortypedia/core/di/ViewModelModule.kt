package dev.sergiosabater.rickmortypedia.core.di

import dev.sergiosabater.rickmortypedia.features.character.presentation.detail.CharacterDetailViewModel
import dev.sergiosabater.rickmortypedia.features.character.presentation.list.CharactersListViewModel
import dev.sergiosabater.rickmortypedia.features.splash.presentation.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        CharacterDetailViewModel(
            getCharacterByIdUseCase = get()
        )
    }

    viewModel {
        CharactersListViewModel(
            getCharactersUseCase = get(),
            searchCharactersUseCase = get()
        )
    }

    viewModel {
        SplashViewModel(
            getCharactersUseCase = get()
        )
    }
}