package com.ktor.api.hirebeat.modules.profile.domain.model

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Genre
import com.ktor.api.hirebeat.modules.users.domain.model.User
import java.util.UUID

data class Profile(
    val id: UUID,
    val user: User,
    val city: String?,
    val experience: Int?,
    val descripcion: String?,
    val imageUrl: String? = null,
    val genres: List<Genre> = emptyList(),
    val instruments: List<ProfileInstrument> = emptyList(),
    val links : List<ProfileLink> = emptyList()
){
    val isComplete: Boolean
        get() = !city.isNullOrBlank() &&
                !descripcion.isNullOrBlank() &&
                instruments.isNotEmpty()
}
