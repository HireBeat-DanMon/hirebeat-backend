package com.ktor.api.hirebeat.modules.catalogs.domain.repository

import com.ktor.api.hirebeat.modules.catalogs.domain.model.*
import java.util.UUID

interface CatalogRepository {

    suspend fun findAllInstruments(): List<Instrument>
    suspend fun findInstrumentByName(name: String): Instrument?
    suspend fun saveInstrument(instrument: Instrument): Instrument

    suspend fun findAllGenres(): List<Genre>
    suspend fun findGenreByName(name: String): Genre?
    suspend fun saveGenre(genre: Genre): Genre

    suspend fun findRoleById(id: UUID): Role?
    suspend fun findAllRoles(): List<Role>
    suspend fun saveRole (role: Role) : Role


}
