package com.ktor.api.hirebeat.modules.gig_requests.application

import com.ktor.api.hirebeat.modules.gig_requests.domain.model.GigRequest
import com.ktor.api.hirebeat.modules.gig_requests.domain.repository.GigRequestRepository
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class GetMusicianGigRequestsUseCase(
    private val gigRequestRepository: GigRequestRepository,
    private val profileRepository: ProfileRepository
) {
    suspend fun execute(userId: UUID): List<GigRequest> = newSuspendedTransaction {
        val profileId = profileRepository.getIdByUserId(userId)
            ?: throw NotFoundException("Perfil de músico no encontrado")

        gigRequestRepository.findByMusicianProfileId(profileId)
    }
}