package com.katest.data.model.message

interface MessageDataSource {

    suspend fun getAllMessages(): List<Message>

    suspend fun insertMessage(message: Message)
}
