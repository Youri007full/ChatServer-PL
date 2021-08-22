package com.example.dto

data class CreateRoomRequest(
    val roomName: String,
    val playerCount: Int
)