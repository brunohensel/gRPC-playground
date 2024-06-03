package dev.brunohensel.app.network.rest

import dev.brunohensel.app.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import java.util.concurrent.TimeUnit

object KtorHttpClientProvider {

    val httpClient by lazy {
        HttpClient(OkHttp) {
            engine { config { callTimeout(30, TimeUnit.SECONDS) } }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "${BuildConfig.LOCAL_HOST}:8080"
                }
                header("accept", "application/json")
            }
            install(Resources)
            install(ContentNegotiation) {
                json()
            }

            install(Logging) {
                logger = Logger.Companion.SIMPLE
                level = LogLevel.HEADERS
            }
        }
    }
}
