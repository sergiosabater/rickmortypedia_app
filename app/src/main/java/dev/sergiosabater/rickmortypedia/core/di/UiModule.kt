package dev.sergiosabater.rickmortypedia.core.di

import dev.sergiosabater.rickmortypedia.core.ui.theme.ThemeManager
import dev.sergiosabater.rickmortypedia.core.ui.theme.ThemePreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val uiModule = module {

    single<ThemePreferences> {
        ThemePreferences(androidContext())
    }

    single<ThemeManager> {
        ThemeManager(
            themePreferences = get()
        )
    }
}