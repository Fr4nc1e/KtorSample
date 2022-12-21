package com.katest.data.model.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
) : UserDataSource {

    private val users = db.getCollection<User>()

    override suspend fun getUserByUserName(userName: String): User? {
        return users.findOne(User::userName eq userName)
    }

    override suspend fun insertNewUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
}