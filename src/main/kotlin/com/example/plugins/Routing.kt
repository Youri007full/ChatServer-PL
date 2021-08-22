package com.example.plugins

import com.example.Route.createRoomRoute
import com.example.Route.getRoom
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {

    install(Routing) {
        createRoomRoute()
        getRoom()
    }
}
