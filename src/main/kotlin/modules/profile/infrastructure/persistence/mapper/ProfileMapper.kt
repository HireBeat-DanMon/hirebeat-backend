package com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.mapper

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Genre
import com.ktor.api.hirebeat.modules.profile.domain.model.*
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.ProfileTable
import com.ktor.api.hirebeat.modules.users.domain.model.User
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toProfile(
    user: User,
    genresList: List<Genre>,
    instrumentsList: List<ProfileInstrument>,
    linksList: List<ProfileLink>
): Profile = Profile(
    id = this[ProfileTable.id],
    user = user,
    city = this[ProfileTable.city],
    experience = this[ProfileTable.experience],
    descripcion = this[ProfileTable.descripcion],
    genres = genresList,
    instruments = instrumentsList,
    links = linksList
)