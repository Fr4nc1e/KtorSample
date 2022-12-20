package com.katest.data.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val toke: String
)
