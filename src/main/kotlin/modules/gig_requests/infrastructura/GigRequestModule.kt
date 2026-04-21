package com.ktor.api.hirebeat.modules.gig_requests.infrastructure

import com.ktor.api.hirebeat.modules.gig_requests.application.*
import com.ktor.api.hirebeat.modules.gig_requests.domain.repository.GigRequestRepository
import com.ktor.api.hirebeat.modules.gig_requests.infrastructure.persistence.PostgresGigRequestRepository
import org.koin.dsl.module

val gigRequestModule = module {
    single<GigRequestRepository> { PostgresGigRequestRepository() }

    factory { CreateGigRequestUseCase(get(), get()) }
    factory { UpdateGigRequestStatusUseCase(get(), get()) }

    factory { GetMusicianGigRequestsUseCase(get(), get()) }
    factory { GetRecruiterGigRequestsUseCase(get()) }
}