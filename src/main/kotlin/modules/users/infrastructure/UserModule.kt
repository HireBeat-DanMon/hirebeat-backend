package com.ktor.api.hirebeat.modules.users.infrastructure

import com.ktor.api.hirebeat.common.infrastructure.security.PasswordHasher
import com.ktor.api.hirebeat.common.infrastructure.security.BCryptHasher
import com.ktor.api.hirebeat.modules.users.application.*
import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository
import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.PostgresUserRepository
import org.koin.dsl.module

val userModule = module {
    factory { GetByIdUserUseCase(get()) }
    factory { GetAllUserUseCase(get()) }
    factory { UpdateUserUseCase(get(), get()) }
    factory { DeleteUserUseCase(get()) }

    single<UserRepository>{ PostgresUserRepository() }
    single<PasswordHasher> { BCryptHasher() }
}
