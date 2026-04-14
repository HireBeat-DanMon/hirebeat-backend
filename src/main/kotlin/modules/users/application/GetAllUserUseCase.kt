package com.ktor.api.hirebeat.modules.users.application

import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository

class GetAllUserUseCase(
    private val repository: UserRepository
){
    suspend fun execute () = repository.findByAll()
}
