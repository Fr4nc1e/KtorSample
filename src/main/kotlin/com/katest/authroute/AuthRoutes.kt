package com.katest.authroute

import com.katest.authroute.authenticate
import com.katest.data.model.User
import com.katest.data.model.UserDataSource
import com.katest.data.requests.AuthRequest
import com.katest.data.response.AuthResponse
import com.katest.security.hashing.HashingService
import com.katest.security.hashing.SaltedHash
import com.katest.security.token.TokenClaim
import com.katest.security.token.TokenConfig
import com.katest.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("signup") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldsBlank = request.userName.isBlank() || request.passWd.isBlank()
        val isPassWdTooShort = request.passWd.length < 8
        if (areFieldsBlank || isPassWdTooShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.passWd)
        val user = User(
            userName = request.userName,
            passWord = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertNewUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByUserName(request.userName)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "No such user.")
            return@post
        }

        val isValidPassword = hashingService.verify(
            request.passWd,
            SaltedHash(
                user.passWord,
                user.salt
            )
        )
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrect password.")
            return@post
        }

        val token = tokenService.generate(
            tokenConfig,
            TokenClaim(
                "userId",
                user.id.toString()
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}