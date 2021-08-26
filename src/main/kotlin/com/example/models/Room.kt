package com.example.models

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.isActive

class Room(
    val roomName: String,
    val maxPLayer: Int,
    var players: List<Player> = listOf()
) {
    var phaseChangedListner: ((Phases) -> Unit)? = null
    private var phase = Phases.WAITING_FOR_PLAYER
        set(value) {
            synchronized(field) {
                field = value
                phaseChangedListner?.let { change ->
                    change(value)
                }
            }
        }

    private fun setPhaseChangedListener(listener: (Phases) -> Unit) {
        phaseChangedListner = listener
    }

    init {
        setPhaseChangedListener { newPhase ->
            when (newPhase) {
                Phases.WAITING_FOR_PLAYER -> waitingForPLayer()
                Phases.WAITING_FOR_START -> waitingForStart()
                else->waitingForStart()
            }
        }
    }

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

    private fun waitingForPLayer() {


    }

    private fun waitingForStart() {


    }

    private fun newRound() {


    }

    private fun runningGame() {


    }

    private fun showWord() {


    }

    enum class Phases {
        WAITING_FOR_PLAYER,
        WAITING_FOR_START,
        NEW_ROUND,
        GAME_RUNNING,
        SHOW_WORD

    }

}