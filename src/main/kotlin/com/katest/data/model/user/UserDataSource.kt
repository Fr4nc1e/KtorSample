package com.katest.data.model.user

import com.katest.data.model.user.User

interface UserDataSource {

    suspend fun getUserByUserName(userName: String): User?

    suspend fun insertNewUser(user: User): Boolean
}