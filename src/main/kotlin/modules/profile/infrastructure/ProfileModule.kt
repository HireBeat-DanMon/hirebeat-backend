package com.ktor.api.hirebeat.modules.profile.infrastructure

import com.ktor.api.hirebeat.modules.profile.application.GetAllProfileUseCase
import com.ktor.api.hirebeat.modules.profile.application.GetByIdProfileUseCase
import com.ktor.api.hirebeat.modules.profile.application.GetMyProfile
import com.ktor.api.hirebeat.modules.profile.application.UpdateProfileUseCase
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.PostgresProfileRepository
import org.koin.dsl.module

val profileModule = module {
    factory { UpdateProfileUseCase(get ()) }
    factory { GetByIdProfileUseCase(get()) }
    factory { GetMyProfile(get()) }
    factory { GetAllProfileUseCase(get()) }

    single<ProfileRepository>{ PostgresProfileRepository() }
}
