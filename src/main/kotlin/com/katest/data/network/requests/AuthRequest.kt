package com.katest.data.network.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val userName: String,
    val passWd: String
)
