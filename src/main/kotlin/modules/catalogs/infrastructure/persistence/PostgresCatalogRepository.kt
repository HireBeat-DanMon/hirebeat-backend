package com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence

import com.ktor.api.hirebeat.modules.catalogs.domain.model.*
import com.ktor.api.hirebeat.modules.catalogs.domain.repository.CatalogRepository
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.mapper.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PostgresCatalogRepository : CatalogRepository {

    override suspend fun findAllInstruments(): List<Instrument> = transaction {
        InstrumentTable.selectAll().map { it.toInstrument() }
    }

    override suspend fun findInstrumentByName(name: String): Instrument? = transaction {
        InstrumentTable.select { InstrumentTable.name eq name }
            .map { it.toInstrument() }
            .singleOrNull()
    }

    override suspend fun saveInstrument(instrument: Instrument): Instrument = transaction {
        val insertedId = InstrumentTable.insert {
            it[name] = instrument.name
        } get InstrumentTable.id

        instrument.copy(id = insertedId)
    }

    override suspend fun findAllGenres(): List<Genre> = transaction {
        GenreTable.selectAll().map { it.toGenre() }
    }

    override suspend fun findGenreByName(name: String): Genre? = transaction {
        GenreTable.select { GenreTable.name eq name }
            .map { it.toGenre() }
            .singleOrNull()
    }

    override suspend fun saveGenre(genre: Genre): Genre = transaction {
        val insertedId = GenreTable.insert {
            it[name] = genre.name
        } get GenreTable.id

        genre.copy(id = insertedId)
    }

    override suspend fun findRoleById(id: UUID): Role? = transaction {
        RoleTable.select { RoleTable.id eq id }
            .map { it.toRole() }
            .singleOrNull()
    }


//    override suspend fun findAllRoles(): List<Role> = transaction {
//        RoleTable.selectAll().map { it.toRole() }
//    }

    override suspend fun findAllRoles(): List<Role> = transaction {
        RoleTable.select {
            RoleTable.name inList listOf("Musician", "Recruiter")
        }.map { it.toRole() }
    }

    override suspend fun saveRole(role: Role): Role = transaction {
        val newId = role.id ?: UUID.randomUUID()
        RoleTable.insert {
            it[id] = newId
            it[name] = role.name ?: throw Exception("BD Problem")
        }
        role.copy(id = newId)
    }
}
