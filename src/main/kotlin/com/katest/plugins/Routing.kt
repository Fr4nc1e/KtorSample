package com.katest.plugins

import com.katest.authroute.authenticate
import com.katest.authroute.getSecretInfo
import com.katest.authroute.signIn
import com.katest.authroute.signUp
import com.katest.data.model.UserDataSource
import com.katest.security.hashing.HashingService
import com.katest.security.token.TokenConfig
import com.katest.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*


fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(hashingService, userDataSource, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
    }
}
