package com.ktor.api.hirebeat.modules.profile.application

import com.ktor.api.hirebeat.common.domain.service.FileStorageService
import java.util.UUID

class UploadProfileImageUseCase(
    private val storageService: FileStorageService,
) {
    suspend fun execute(userId: UUID, fileBytes: ByteArray, originalName: String): String {
        val fileName = "${userId}_${System.currentTimeMillis()}_$originalName"
        val imageUrl = storageService.uploadImage(fileBytes, fileName)

        return imageUrl
    }
}