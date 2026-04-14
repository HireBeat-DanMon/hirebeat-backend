package com.ktor.api.hirebeat.modules.profile.application

import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository

class GetAllProfileUseCase(
    private val repository: ProfileRepository
){
    suspend fun execute() = repository.findAll();
}
