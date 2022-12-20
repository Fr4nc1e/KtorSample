package com.katest.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val userName: String,
    val passWd: String
)
