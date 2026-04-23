package com.ktor.api.hirebeat.modules.gig_requests.infrastructure.persistence

import com.ktor.api.hirebeat.modules.gig_requests.domain.model.RequestStatus
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.ProfileTable
import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.UserTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object GigRequestTable : Table("solicitudes_suplencia") {
    val id = uuid("id")
    val recruiterId = uuid("reclutador_id") references UserTable.id
    val musicianProfileId = uuid("musico_id") references ProfileTable.id
    val startTime = datetime("fecha_inicio")
    val endTime = datetime("fecha_fin")
    val location = varchar("lugar", 255)
    val paymentOffered = double("pago_ofrecido")
    val messageDetails = text("mensaje_detalles").nullable()
    val status = enumerationByName("estado", 20, RequestStatus::class).default(RequestStatus.PENDIENTE)

    val createdAt = datetime("fecha_creacion").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("fecha_actualizacion").clientDefault { LocalDateTime.now() }

    override val primaryKey = PrimaryKey(id)
}