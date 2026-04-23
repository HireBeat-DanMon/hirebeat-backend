package com.ktor.api.hirebeat.modules.profile.application

import com.ktor.api.hirebeat.common.domain.service.FileStorageService
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class UploadProfileImageUseCase(
    private val storageService: FileStorageService,
    private val repository: ProfileRepository
) {
    suspend fun execute(userId: UUID, fileBytes: ByteArray, originalName: String): String {

        val profileId = newSuspendedTransaction {
            repository.getIdByUserId(userId)
                ?: throw NotFoundException("No se encontró el perfil para el usuario $userId")
        }

        val fileName = "${userId}_${System.currentTimeMillis()}_$originalName"
        val imageUrl = storageService.uploadImage(fileBytes, fileName)

        val saved = newSuspendedTransaction {
            repository.updateImageUrl(profileId, imageUrl)
        }

        if (!saved) throw Exception("No se pudo guardar la URL de la imagen en la BD")

        return imageUrl
    }
}