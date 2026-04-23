package com.ktor.api.hirebeat.modules.profile.domain.repository

import com.ktor.api.hirebeat.modules.profile.domain.model.Profile
import java.util.UUID

interface ProfileRepository{

    suspend fun findById (id : UUID) : Profile?
    suspend fun findAll () : List<Profile>
    suspend fun getIdByUserId(userId: UUID): UUID?
    suspend fun create (id : UUID) : Boolean
    suspend fun update (profile: Profile) : Boolean
    suspend fun updateImageUrl(profileId: UUID, imageUrl: String): Boolean
}
