package com.ktor.api.hirebeat.modules.availability.infrastructure.persistence

import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.ProfileTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object BlockedSlotTable : Table("fechas_bloqueadas") {
    val id = uuid("id")
    val profileId = uuid("perfil_id") references ProfileTable.id
    val startTime = datetime("fecha_inicio")
    val endTime = datetime("fecha_fin")
    val reason = varchar("motivo", 255).nullable()
    val isAutomatic = bool("bloqueo_automatico").default(false)

    override val primaryKey = PrimaryKey(id)
}