package com.example.Route

import com.example.dto.BasicApiResponse
import com.example.dto.CreateRoomRequest
import com.example.dto.RoomResponse
import com.example.models.Room
import com.example.others.Constants
import com.example.server
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createRoomRoute() {
    route("/api/addRoom") {
        post {
            val roomRequest = call.receiveOrNull<CreateRoomRequest>()

            if (roomRequest == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (server.rooms[roomRequest.roomName] != null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(false, "Room already exist with the Same Room Name")
                )
            }
            if (roomRequest.playerCount < 2) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(false, "Room must have more then one Player")
                )
            }
            if (roomRequest.playerCount > Constants.maxPlayer) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(false, "Room Player must less then eight")
                )
            }

            val room = Room(roomRequest.roomName, roomRequest.playerCount)
            server.rooms[room.roomName] = room
            call.respond(HttpStatusCode.OK)
        }

    }
}

fun Route.getRoom() {
    route("/api/room") {
        get {
            val searchQuery = call.parameters["searchQuery"]
            if (searchQuery == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val filteredRoom = server.rooms.filterKeys { roomName ->
                roomName.contains(searchQuery, ignoreCase = true)
            }
            val roomResponse = filteredRoom.values.map {
                RoomResponse(it.roomName, it.maxPLayer, it.players.size)
            }.sortedBy { it.roomName }

            call.respond(HttpStatusCode.OK, roomResponse)
        }

    }
}

fun Route.addPlayer() {
    route("/api/addPlayer") {
        get {
            val playerName = call.parameters["playerName"]
            val roomName = call.parameters["roomName"]
            if (playerName == null || roomName == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val room = server.rooms[roomName]
            when {
                room == null -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(true, "Room Not Found")
                    )
                }
                room.containsPlayer(playerName) -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(true, "There is already a player with the same name")
                    )
                }
                room.players.size >= room.maxPLayer -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(true, "Room is full")
                    )
                }
                else ->
                    call.respond(HttpStatusCode.OK, BasicApiResponse(true))
            }

        }
    }
}
