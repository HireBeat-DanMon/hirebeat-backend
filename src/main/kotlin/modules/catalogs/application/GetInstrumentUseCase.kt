package com.ktor.api.hirebeat.modules.catalogs.application

import com.ktor.api.hirebeat.modules.catalogs.domain.repository.CatalogRepository

class GetInstrumentUseCase(
    private val repository: CatalogRepository
) {
    suspend fun execute () = repository.findAllInstruments()
}