package net.hyren.core.shared.users.punishments.storage.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.users.punishments.storage.dao.UserPunishmentDAO
import net.hyren.core.shared.users.punishments.storage.dto.CreateUserPunishmentDTO
import net.hyren.core.shared.users.punishments.storage.dto.FetchUserPunishmentByIdDTO
import net.hyren.core.shared.users.punishments.storage.dto.FetchUserPunishmentsByUserIdDTO
import net.hyren.core.shared.users.punishments.storage.dto.UpdateUserPunishmentByIdDTO
import net.hyren.core.shared.users.punishments.storage.repositories.IUsersPunishmentsRepository
import net.hyren.core.shared.users.punishments.storage.table.UsersPunishmentsTable
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgreSQLUsersPunishmentsRepository : IUsersPunishmentsRepository {

    override fun fetchByUserId(
        fetchUserPunishmentsByUserIdDTO: FetchUserPunishmentsByUserIdDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserPunishmentDAO.find {
            UsersPunishmentsTable.userId eq fetchUserPunishmentsByUserIdDTO.userId
        }.map { it.toUserPunishment() }
    }

    override fun fetchById(
        fetchUserPunishmentsByIdDTO: FetchUserPunishmentByIdDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserPunishmentDAO.find {
            UsersPunishmentsTable.id eq fetchUserPunishmentsByIdDTO.id
        }.firstOrNull()?.toUserPunishment()
    }

    override fun create(
        createUserPunishmentDTO: CreateUserPunishmentDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserPunishmentDAO.new {
            this.userId = createUserPunishmentDTO.userId
            this.stafferId = createUserPunishmentDTO.stafferId
            this.punishType = createUserPunishmentDTO.punishType
            this.punishCategory = createUserPunishmentDTO.punishCategory
            this.duration = createUserPunishmentDTO.duration
            this.customReason = createUserPunishmentDTO.customReason
            this.proof = createUserPunishmentDTO.proof
            this.perpetual = createUserPunishmentDTO.perpetual
            this.hidden = createUserPunishmentDTO.hidden
        }.toUserPunishment()
    }

    override fun update(
        updateUserPunishmentByIdDTO: UpdateUserPunishmentByIdDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        UserPunishmentDAO.find {
            UsersPunishmentsTable.id eq updateUserPunishmentByIdDTO.id
        }.firstOrNull()?.let {
            updateUserPunishmentByIdDTO.execute(it)

            // enhance this later...
            true
        } ?: false
    }

}