package dev.sergiosabater.rickmortypedia

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import dev.sergiosabater.rickmortypedia.core.di.coilModule
import dev.sergiosabater.rickmortypedia.core.di.databaseModule
import dev.sergiosabater.rickmortypedia.core.di.navigationModule
import dev.sergiosabater.rickmortypedia.core.di.networkModule
import dev.sergiosabater.rickmortypedia.core.di.repositoryModule
import dev.sergiosabater.rickmortypedia.core.di.uiModule
import dev.sergiosabater.rickmortypedia.core.di.useCaseModule
import dev.sergiosabater.rickmortypedia.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RickMortyPediaApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RickMortyPediaApp)

            androidLogger(Level.ERROR)

            modules(
                networkModule,
                databaseModule,
                repositoryModule,
                useCaseModule,
                coilModule,
                uiModule,
                viewModelModule,
                navigationModule
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("coil_disk_cache"))
                    .maxSizeBytes(100 * 1024 * 1024)
                    .build()
            }
            .respectCacheHeaders(false)
            .build()
    }
}