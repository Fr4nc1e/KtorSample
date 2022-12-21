package com.katest.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val toke: String
)
