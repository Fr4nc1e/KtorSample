package com.katest.plugins

import com.katest.data.network.authenticate
import com.katest.data.network.getSecretInfo
import com.katest.data.network.signIn
import com.katest.data.network.signUp
import com.katest.data.model.user.UserDataSource
import com.katest.security.hashing.HashingService
import com.katest.security.token.TokenConfig
import com.katest.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.application.*


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
