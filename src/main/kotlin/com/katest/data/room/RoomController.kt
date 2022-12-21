package com.katest.data.room

import com.katest.data.model.message.Message
import com.katest.data.model.message.MessageDataSource
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        userName: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        members[userName] = Member(
            userName = userName,
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun sendMessage(
        senderUserName: String,
        message: String
    ) {
        members.values
            .forEach {
                val messageEntity = Message(
                    text = message,
                    userName = senderUserName,
                    timeStamp = System.currentTimeMillis()
                )
                messageDataSource.insertMessage(messageEntity)

                val parsedMessage = Json.encodeToString(messageEntity)
                it.socket.send(Frame.Text(parsedMessage))
            }
    }

    suspend fun getAllMessages(): List<Message> = messageDataSource.getAllMessages()

    suspend fun tryDisconnect(userName: String) {
        members[userName]?.socket?.close()
        if (members.containsKey(userName)) {
            members.remove(userName)
        }
    }
}
