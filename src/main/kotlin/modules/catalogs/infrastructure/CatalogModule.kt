package com.ktor.api.hirebeat.modules.catalogs.infrastructure

import com.ktor.api.hirebeat.modules.catalogs.application.GetGenreUseCase
import com.ktor.api.hirebeat.modules.catalogs.application.GetInstrumentUseCase
import com.ktor.api.hirebeat.modules.catalogs.application.GetProfileOptionsUseCase
import com.ktor.api.hirebeat.modules.catalogs.application.GetRoleUseCase
import com.ktor.api.hirebeat.modules.catalogs.domain.repository.CatalogRepository
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.PostgresCatalogRepository
import org.koin.dsl.module

val catalogModule = module {
    single<CatalogRepository> { PostgresCatalogRepository() }

    factory { GetGenreUseCase(get()) }
    factory { GetInstrumentUseCase(get()) }
    factory { GetRoleUseCase(get()) }
    factory { GetProfileOptionsUseCase () }
}