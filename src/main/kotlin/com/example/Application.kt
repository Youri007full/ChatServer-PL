package com.example

import DrawingService
import com.example.models.DataSession
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureMonitoring()
        configureRouting()
        configureSockets()
        configureSerialization()
    }.start(wait = true)
}

val server = DrawingService()

fun Application.module() {
    install(Sessions) {
        cookie<DataSession>("user_session")
    }

    intercept(ApplicationCallPipeline.Features) {
        if (call.sessions.get<DataSession>() == null) {
            val clientId = call.parameters["client_id"] ?: ""
            call.sessions.set(DataSession(generateNonce(), clientId))
        }
    }

}