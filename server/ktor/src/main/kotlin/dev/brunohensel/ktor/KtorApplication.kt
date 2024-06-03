package dev.brunohensel.ktor

import dev.brunohensel.ktor.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json(DefaultJson)
        }

        module()
    }.start(wait = true)
}

fun Application.module() {
    configureRouting()
}
