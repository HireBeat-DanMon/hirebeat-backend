package com.ktor.api.hirebeat.modules.profile.infrastructure.persistence

import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.InstrumentTable
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.ProfileTable.id
import org.jetbrains.exposed.sql.Table

object ProfileInstrumentsTable : Table("musical_instruments") {
    val profileId = uuid("id")
    val instrumentId = integer("instrument_id").references(InstrumentTable.id)
    val level = integer("level")
    val isPrincipal = bool("isPrincipal").default(false)
}