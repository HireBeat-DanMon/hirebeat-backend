package com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.mapper

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Genre
import com.ktor.api.hirebeat.modules.catalogs.domain.model.Instrument
import com.ktor.api.hirebeat.modules.catalogs.domain.model.Role
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.GenreTable
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.InstrumentTable
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.RoleTable
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toInstrument() = Instrument(
    id = this[InstrumentTable.id],
    name = this[InstrumentTable.name]
)

fun ResultRow.toGenre() = Genre(
    id = this[GenreTable.id],
    name = this[GenreTable.name]
)

fun ResultRow.toRole(): Role = Role(
    id = this[RoleTable.id],
    name = this[RoleTable.name]
)