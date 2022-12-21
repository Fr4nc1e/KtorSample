package com.katest.plugins

import com.katest.data.model.session.ChatSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSessions() {
    install(Sessions) {
        cookie<ChatSession>("SESSION")
    }

    intercept(ApplicationCallPipeline.Plugins) {
        if (call.sessions.get<ChatSession>() == null) {
            val userName = call.parameters["userName"] ?: "Guest"
            call.sessions.set(ChatSession(userName, generateNonce()))
        }
    }
}