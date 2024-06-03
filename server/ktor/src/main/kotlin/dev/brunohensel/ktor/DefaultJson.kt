package dev.brunohensel.ktor

import kotlinx.serialization.json.Json

val DefaultJson by lazy {
    Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }
}
