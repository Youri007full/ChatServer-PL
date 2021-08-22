package com.example.models

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.isActive

class Room(
    val roomName: String,
    val maxPLayer: Int,
    var players: List<Player> = listOf()
) {

    suspend fun brodcast(message: String) {
        players.forEach { player ->
            if (player.socket.isActive) {
                player.socket.send(Frame.Text(message))
            }
        }
    }

    suspend fun brodcasrExceptOne(message: String, clientId: String) {
        players.forEach { player ->
            if (player.socket.isActive && player.clientId != clientId) {
                player.socket.send(Frame.Text(message))
            }
        }
    }

    fun containsPlayer(username: String): Boolean {
        return players.find { it.userName == username } != null
    }

}