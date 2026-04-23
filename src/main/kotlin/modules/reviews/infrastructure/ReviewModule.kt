package com.ktor.api.hirebeat.modules.reviews.infrastructure

import com.ktor.api.hirebeat.modules.reviews.application.*
import com.ktor.api.hirebeat.modules.reviews.domain.repository.ReviewRepository
import com.ktor.api.hirebeat.modules.reviews.infrastructure.persistence.PostgresReviewRepository
import org.koin.dsl.module

val reviewModule = module {
    single<ReviewRepository> { PostgresReviewRepository() }
    factory { CreateReviewUseCase(get()) }
    factory { GetProfileReviewsUseCase(get()) }
    factory { GetMyReviewsUseCase(get(), get()) }
}