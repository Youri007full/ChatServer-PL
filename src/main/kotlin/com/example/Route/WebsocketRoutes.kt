package com.example.Route

import com.example.dto.ChatMessage
import com.example.gson
import com.example.models.BaseModel
import com.example.models.DataSession
import com.google.gson.JsonParser
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import java.lang.Exception

fun Route.handleWebsocket(
    handle: suspend (
        socket: WebSocketServerSession,
        clientId: String,
        message: String,
        payload: BaseModel
    ) -> Unit
) {
    webSocket {
        val session = call.sessions.get<DataSession>()

        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "client is disconnected"))
            return@webSocket
        }
        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    val jsonObject = JsonParser.parseString(message).asJsonObject
                    val type = when (jsonObject.get("type").asString) {
                        "ChatObject" -> {
                            ChatMessage::class.java
                        }
                        else -> BaseModel::class.java
                    }
                    val payload = gson.fromJson(message, type)
                    handle(this,session.clientId,message,payload)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {

        }
    }

}