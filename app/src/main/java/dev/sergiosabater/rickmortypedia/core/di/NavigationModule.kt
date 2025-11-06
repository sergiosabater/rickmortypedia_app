package dev.sergiosabater.rickmortypedia.core.di

import dev.sergiosabater.rickmortypedia.core.navigation.AppNavigator
import org.koin.dsl.module

val navigationModule = module {
    single<AppNavigator> {
        AppNavigator()
    }
}