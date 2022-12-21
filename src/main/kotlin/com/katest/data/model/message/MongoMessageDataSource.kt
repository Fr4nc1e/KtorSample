package com.katest.data.model.message

import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoMessageDataSource(
    private val db: CoroutineDatabase
): MessageDataSource {

    private val messages = db.getCollection<Message>()
    override suspend fun getAllMessages(): List<Message> {
        return messages.find()
            .descendingSort(Message::timeStamp)
            .toList()
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }
}