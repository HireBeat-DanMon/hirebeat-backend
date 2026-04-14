package com.ktor.api.hirebeat.modules.profile.infrastructure.persistence

import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.GenreTable
import org.jetbrains.exposed.sql.Table

object ProfileGenresTable : Table("musical_genres") {
    val profileId = uuid("porfile_id").references(ProfileTable.id)
    val genreId = integer("genres_id").references(GenreTable.id)
}