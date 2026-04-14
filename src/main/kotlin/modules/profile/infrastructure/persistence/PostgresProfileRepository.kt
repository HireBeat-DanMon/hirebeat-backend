package com.ktor.api.hirebeat.modules.profile.infrastructure.persistence

import com.ktor.api.hirebeat.modules.catalogs.domain.model.*
import com.ktor.api.hirebeat.modules.catalogs.infrastructure.persistence.*
import com.ktor.api.hirebeat.modules.profile.domain.model.*
import com.ktor.api.hirebeat.modules.profile.domain.repository.ProfileRepository
import com.ktor.api.hirebeat.modules.profile.infrastructure.persistence.mapper.toProfile
import com.ktor.api.hirebeat.modules.users.domain.model.User
import com.ktor.api.hirebeat.modules.users.infrastructure.persistence.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class PostgresProfileRepository : ProfileRepository {

    override suspend fun findById(id: UUID): Profile? {
        val profileRow = (ProfileTable innerJoin UserTable)
            .select { ProfileTable.id eq id }
            .singleOrNull() ?: return null

        val genres = (ProfileGenresTable innerJoin GenreTable)
            .select { ProfileGenresTable.profileId eq id }
            .map { Genre(it[GenreTable.id], it[GenreTable.name]) }

        val instruments = (ProfileInstrumentsTable innerJoin InstrumentTable)
            .select { ProfileInstrumentsTable.profileId eq id }
            .map {
                ProfileInstrument(
                    Instrument(it[InstrumentTable.id], it[InstrumentTable.name]),
                    Level.fromInt(it[ProfileInstrumentsTable.level]),
                    it[ProfileInstrumentsTable.isPrincipal]
                )
            }

        val links = ProfileLinksTable
            .select { ProfileLinksTable.profileId eq id }
            .map {
                ProfileLink(
                    name = it[ProfileLinksTable.socialMedia],
                    type = LinkType.fromInt(it[ProfileLinksTable.type]),
                    ref = it[ProfileLinksTable.link]
                )
            }

        val user = User(
            id = profileRow[UserTable.id],
            fullname = profileRow[UserTable.fullname],
            email = profileRow[UserTable.email],
            password = ""
        )

        return profileRow.toProfile(user, genres, instruments, links)
    }

    override suspend fun create(userIdNow: UUID): Boolean = transaction {
        val newId = UUID.randomUUID()

        val insertedRows = ProfileTable.insert {
            it[id] = newId
            it[userId] = userIdNow
        }
        insertedRows.insertedCount > 0
    }

    override suspend fun update(profile: Profile): Boolean {
        val updatedRows = ProfileTable.update({ ProfileTable.id eq profile.id }) {
            it[city] = profile.city
            it[experience] = profile.experience
            it[descripcion] = profile.descripcion
        }

        if (updatedRows > 0) {
            insertInstrument(profile)
            insertGenre(profile)
            insertLinks(profile)
        }
        return updatedRows > 0
    }

    override suspend fun getIdByUserId(userId: UUID): UUID? {
        return ProfileTable
            .slice(ProfileTable.id)
            .select { ProfileTable.userId eq userId }
            .map { it[ProfileTable.id] }
            .singleOrNull()
    }

    override suspend fun findAll(): List<Profile> = transaction {
        val profilesRows = (ProfileTable innerJoin UserTable)
            .selectAll()
            .toList()

        profilesRows.map { row ->
            val profileId = row[ProfileTable.id]

            val genres = (ProfileGenresTable innerJoin GenreTable)
                .select { ProfileGenresTable.profileId eq profileId }
                .map { Genre(it[GenreTable.id], it[GenreTable.name]) }

            val instruments = (ProfileInstrumentsTable innerJoin InstrumentTable)
                .select {
                    (ProfileInstrumentsTable.profileId eq profileId) and
                            (ProfileInstrumentsTable.isPrincipal eq true)
                }
                .map {
                    ProfileInstrument(
                        Instrument(it[InstrumentTable.id], it[InstrumentTable.name]),
                        Level.fromInt(it[ProfileInstrumentsTable.level]),
                        it[ProfileInstrumentsTable.isPrincipal]
                    )
                }

            val user = User(
                id = row[UserTable.id],
                fullname = row[UserTable.fullname],
                email = row[UserTable.email],
                password = ""
            )

            row.toProfile(user, genres, instruments, emptyList())
        }
    }

    private fun insertInstrument(profile: Profile){
        ProfileInstrumentsTable.deleteWhere { ProfileInstrumentsTable.profileId eq profile.id }

        if (profile.instruments.isNotEmpty()) {
            ProfileInstrumentsTable.batchInsert(profile.instruments) { inst ->
                this[ProfileInstrumentsTable.profileId] = profile.id
                this[ProfileInstrumentsTable.instrumentId] = inst.instrument.id
                this[ProfileInstrumentsTable.level] = inst.level.rank
                this[ProfileInstrumentsTable.isPrincipal] = inst.isPrincipal
            }
        }
    }

    private fun insertLinks(profile: Profile) {
        ProfileLinksTable.deleteWhere { ProfileLinksTable.profileId eq profile.id }
        if (profile.links.isNotEmpty()){
            ProfileLinksTable.batchInsert(data = profile.links){ l ->
                this[ProfileLinksTable.profileId] = profile.id
                this[ProfileLinksTable.socialMedia] = l.name
                this[ProfileLinksTable.type] = l.type.id
                this[ProfileLinksTable.link] = l.ref
            }
        }
    }

    private fun insertGenre(profile: Profile){
        ProfileGenresTable.deleteWhere { ProfileGenresTable.profileId eq profile.id }

        if (profile.genres.isNotEmpty()) {
            ProfileGenresTable.batchInsert(profile.genres) { genre ->
                this[ProfileGenresTable.profileId] = profile.id
                this[ProfileGenresTable.genreId] = genre.id
            }
        }
    }

}