package com.ktor.api.hirebeat.modules.auth.infrastructure

import com.ktor.api.hirebeat.modules.auth.application.*
import com.ktor.api.hirebeat.common.infrastructure.security.PasswordHasher
import com.ktor.api.hirebeat.modules.auth.domain.service.TokenService
import com.ktor.api.hirebeat.modules.auth.infrastructure.security.JwtTokenService
import com.ktor.api.hirebeat.common.infrastructure.security.BCryptHasher
import com.ktor.api.hirebeat.modules.catalogs.domain.repository.CatalogRepository
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.PostgresCatalogRepository
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.PostgresProfileRepository
import com.ktor.api.hirebeat.modules.users.domain.repository.UserRepository
import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.PostgresUserRepository
import org.koin.dsl.module

val authModule = module {
    single<PasswordHasher> { BCryptHasher() }
    single<TokenService> { JwtTokenService() }
    single<UserRepository> { PostgresUserRepository() }
    single<CatalogRepository> { PostgresCatalogRepository() }
    single<ProfileRepository> { PostgresProfileRepository() }

    factory { LoginUseCase(get(), get(), get()) }
    factory { RegisterUseCase(get(), get(), get(), get())}
}