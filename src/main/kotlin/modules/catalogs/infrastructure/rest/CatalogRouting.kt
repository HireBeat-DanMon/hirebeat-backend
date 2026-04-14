package com.ktor.api.hirebeat.modules.catalogs.infrastructure.rest

import com.ktor.api.hirebeat.modules.catalogs.application.*
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.rest.dto.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.catalogRouting() {
    val getGenreUseCase by inject<GetGenreUseCase>()
    val getInstrumentUseCase by inject<GetInstrumentUseCase>()
    val getRoleUseCase by inject <GetRoleUseCase>()
    val getProfileOptionsUseCase by inject<GetProfileOptionsUseCase>()

    get("/profile-options") {
        val (levels, linkTypes) = getProfileOptionsUseCase.execute()

        val response = mapOf(
            "levels" to levels.map { it.toResponse() },
            "linkTypes" to linkTypes.map { it.toResponse() }
        )

        call.respond(HttpStatusCode.OK, response)
    }

    get("instruments") {
        val instruments = getInstrumentUseCase.execute()
        val response = instruments?.map { it.toResponse() } ?: emptyList()
        call.respond(HttpStatusCode.OK, response)
    }

    get("genre") {
        val genre = getGenreUseCase.execute()
        val response = genre?.map { it.toResponse() } ?: emptyList()
        call.respond(HttpStatusCode.OK, response)
    }

    get("roles"){
        val roles = getRoleUseCase.execute()
        val response = roles?.map { it.toResponse() } ?: emptyList()
        call.respond(HttpStatusCode.OK, response)
    }
}
