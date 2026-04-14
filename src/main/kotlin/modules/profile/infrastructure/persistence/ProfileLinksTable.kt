package com.ktor.api.hirebeat.modules.profile.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object ProfileLinksTable : Table ("musical_plataform"){
    val profileId = uuid("id")
    val socialMedia = varchar("social_media", 100)
    val type = integer("type")
    val link = varchar("link", 100)
}