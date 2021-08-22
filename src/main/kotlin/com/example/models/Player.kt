package com.example.models

import io.ktor.http.cio.websocket.*

data class Player(
    val clientId: String,
    val userName: String,
    var rank: Int,
    var socket: WebSocketSession,
    var isDrawing: Boolean,
    var score: String
)
