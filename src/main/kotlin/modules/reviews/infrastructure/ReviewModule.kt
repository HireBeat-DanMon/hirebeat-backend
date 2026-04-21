package com.ktor.api.hirebeat.modules.reviews.infrastructure

import com.ktor.api.hirebeat.common.domain.service.FileStorageService
import com.ktor.api.hirebeat.common.infrastructure.media.CloudinaryService
import com.ktor.api.hirebeat.modules.profile.application.UploadProfileImageUseCase
import com.ktor.api.hirebeat.modules.reviews.application.CreateReviewUseCase
import com.ktor.api.hirebeat.modules.reviews.application.GetProfileReviewsUseCase
import com.ktor.api.hirebeat.modules.reviews.domain.repository.ReviewRepository
import com.ktor.api.hirebeat.modules.reviews.infrastructure.persistence.PostgresReviewRepository
import org.koin.dsl.module

val reviewModule = module {
    single<ReviewRepository> { PostgresReviewRepository() }
    factory { CreateReviewUseCase(get()) }
    factory { GetProfileReviewsUseCase(get()) }
    single<FileStorageService> { CloudinaryService() }
    factory { UploadProfileImageUseCase(get()) }
}