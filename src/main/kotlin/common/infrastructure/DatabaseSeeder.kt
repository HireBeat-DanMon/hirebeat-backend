package com.ktor.api.hirebeat.common.infrastructure

import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.GenreTable
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.InstrumentTable
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.RoleTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object DatabaseSeeder {
    fun seed() {
        transaction {
            seedRoles()
            seedInstruments()
            seedGenres()
        }
    }

    private fun seedRoles() {
        if (RoleTable.selectAll().empty()) {
            val admin = UUID.randomUUID()
            val roles = listOf(
                UUID.fromString("11111111-1111-1111-1111-111111111111") to "Recruiter",
                UUID.fromString("22222222-2222-2222-2222-222222222222") to "Musician",
                admin to "SuperAdmin"
            )

            roles.forEach { (fixedId, roleName) ->
                RoleTable.insert {
                    it[id] = fixedId
                    it[name] = roleName
                }
            }
        }
    }

    private fun seedInstruments() {
        if(InstrumentTable.selectAll().empty()){
            val instruments = listOf(
                "Tololoche",
                "Acordeon",
                "Marimba",
                "Vihuela",
                "Guitarron",
                "Tompeta")
            instruments.forEach { name ->
                InstrumentTable.insert { it[InstrumentTable.name] = name }
            }
        }
    }

    private fun seedGenres() {
        if(GenreTable.selectAll().empty()){
            val genre = listOf(
                "Norteño",
                "Mariachi",
                "Marimba Chiapaneca",
                "Salsa",
                "Cumbia",
                "Rock")
            genre.forEach { name ->
                GenreTable.insert { it[GenreTable.name] = name }
            }
        }
    }
}