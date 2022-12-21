package com.katest.data.model.user

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val userName: String,
    val passWord: String,
    val salt: String,
    @BsonId val id: ObjectId = ObjectId()
)
