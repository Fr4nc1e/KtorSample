package com.katest.data.model

interface UserDataSource {

    suspend fun getUserByUserName(userName: String): User?

    suspend fun insertNewUser(user: User): Boolean
}