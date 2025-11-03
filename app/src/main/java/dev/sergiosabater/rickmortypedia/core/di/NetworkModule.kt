package dev.sergiosabater.rickmortypedia.core.di

import android.util.Log
import dev.sergiosabater.rickmortypedia.core.network.BASE_URL
import dev.sergiosabater.rickmortypedia.core.network.CONNECTION_TIMEOUT
import dev.sergiosabater.rickmortypedia.core.network.RickAndMortyApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    single<HttpClient> {
        HttpClient(Android) {

            // Logging plugin
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KtorClient", message)
                    }
                }
                level = LogLevel.ALL
            }

            // Content negotiation plugin (JSON)
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            // Timeout plugin
            install(HttpTimeout) {
                requestTimeoutMillis = CONNECTION_TIMEOUT * 1000L
                connectTimeoutMillis = CONNECTION_TIMEOUT * 1000L
                socketTimeoutMillis = CONNECTION_TIMEOUT * 1000L
            }

            // Default request configuration
            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }

    single<RickAndMortyApiService> {
        RickAndMortyApiService(
            httpClient = get()
        )
    }
}