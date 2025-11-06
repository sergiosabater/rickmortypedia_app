package dev.sergiosabater.rickmortypedia.core.di

import coil.ImageLoader
import dev.sergiosabater.rickmortypedia.RickMortyPediaApp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val coilModule = module {

    single<ImageLoader> {
        val app = androidApplication() as RickMortyPediaApp
        app.newImageLoader()
    }
}