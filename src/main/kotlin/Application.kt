package com.ktor.api.hirebeat

import com.ktor.api.hirebeat.common.infrastructure.DatabaseFactory
import com.ktor.api.hirebeat.common.infrastructure.security.configureSecurity
import com.ktor.api.hirebeat.modules.auth.infrastructure.authModule
import com.ktor.api.hirebeat.modules.auth.infrastructure.rest.authRouting
import com.ktor.api.hirebeat.modules.auth.infrastructure.security.JwtTokenService
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.catalogModule
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.rest.catalogRouting
import com.ktor.api.hirebeat.modules.profile.infrastructure.profileModule
import com.ktor.api.hirebeat.modules.profile.infrastructure.rest.profileRouting
import com.ktor.api.hirebeat.modules.reviews.infrastructure.rest.reviewRouting
import com.ktor.api.hirebeat.modules.reviews.infrastructure.reviewModule
import com.ktor.api.hirebeat.modules.users.infrastructure.userModule
import com.ktor.api.hirebeat.modules.users.infrastructure.rest.userRouting
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

}

fun Application.module() {
    DatabaseFactory.init()

    install(Koin) {
        slf4jLogger()
        modules(
            userModule,
            authModule,
            catalogModule,
            profileModule,
            reviewModule
        )
    }

    configureSecurity()

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Post)
    }

    routing {
        userRouting()
        authRouting()
        catalogRouting()
        profileRouting()
        reviewRouting()
    }

    println("HireBeat API running port 8080")
}
