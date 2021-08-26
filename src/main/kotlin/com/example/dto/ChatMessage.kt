package com.example.dto

import com.example.models.BaseModel

data class ChatMessage(
    val from :String,
    val message: String,
    val roomName:String,
    val timestamp: Long
) : BaseModel("ChatObject")
