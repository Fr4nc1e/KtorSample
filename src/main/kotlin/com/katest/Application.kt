package com.katest

import com.katest.data.model.MongoUserDataSource
import com.katest.plugins.*
import com.katest.security.hashing.SHA256HashingService
import com.katest.security.token.JwtTokenService
import com.katest.security.token.TokenConfig
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val mongoPasswd = System.getenv("MONGO_PW")
    val dbName = "ktor-auth"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://admin:$mongoPasswd@cluster0.i7nzu4x.mongodb.net/?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        environment.config.property("jwt.issuer").getString(),
        environment.config.property("jwt.audience").getString(),
        365L * 1000L * 60L * 60L * 24L,
        System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSecurity(tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureRouting(
        userDataSource,
        hashingService,
        tokenService,
        tokenConfig
    )
}
