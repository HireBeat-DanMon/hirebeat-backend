package com.ktor.api.hirebeat.modules.availability.infrastructure

import com.ktor.api.hirebeat.modules.availability.application.AddBlockedSlotUseCase
import com.ktor.api.hirebeat.modules.availability.application.DeleteBlockedSlotUseCase
import com.ktor.api.hirebeat.modules.availability.application.GetProfileAvailabilityUseCase
import com.ktor.api.hirebeat.modules.availability.domain.repository.BlockedSlotRepository
import com.ktor.api.hirebeat.modules.availability.infrastructure.persistence.PostgresBlockedSlotRepository
import org.koin.dsl.module

val availabilityModule = module {
    single<BlockedSlotRepository> { PostgresBlockedSlotRepository() }

    factory { AddBlockedSlotUseCase(get(), get()) }
    factory { GetProfileAvailabilityUseCase(get()) }
    factory { DeleteBlockedSlotUseCase(get(), get()) }
}