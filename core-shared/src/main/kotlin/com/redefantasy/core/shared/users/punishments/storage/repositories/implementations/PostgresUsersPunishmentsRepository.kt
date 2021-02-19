package com.redefantasy.core.shared.users.punishments.storage.repositories.implementations

import com.redefantasy.core.shared.users.punishments.data.UserPunishment
import com.redefantasy.core.shared.users.punishments.storage.dao.UserPunishmentDAO
import com.redefantasy.core.shared.users.punishments.storage.dto.CreateUserPunishmentDTO
import com.redefantasy.core.shared.users.punishments.storage.dto.FetchUserPunishmentByIdDTO
import com.redefantasy.core.shared.users.punishments.storage.dto.FetchUserPunishmentsByUserIdDTO
import com.redefantasy.core.shared.users.punishments.storage.dto.UpdateUserPunishmentByIdDTO
import com.redefantasy.core.shared.users.punishments.storage.repositories.IUsersPunishmentsRepository
import com.redefantasy.core.shared.users.punishments.storage.table.UsersPunishmentsTable
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresUsersPunishmentsRepository : IUsersPunishmentsRepository {

    override fun fetchByUserId(fetchUserPunishmentsByUserIdDTO: FetchUserPunishmentsByUserIdDTO): List<UserPunishment> {
        return transaction {
            val userPunishments = mutableListOf<UserPunishment>()

            UserPunishmentDAO.find {
                UsersPunishmentsTable.userId eq fetchUserPunishmentsByUserIdDTO.userId
            }.forEach {
                userPunishments.add(it.asUserPunishment())
            }

            return@transaction userPunishments
        }
    }

    override fun fetchById(fetchUserPunishmentsByIdDTO: FetchUserPunishmentByIdDTO): UserPunishment? {
        return transaction {
            var userPunishment: UserPunishment? = null

            val result = UserPunishmentDAO.find {
                UsersPunishmentsTable.id eq fetchUserPunishmentsByIdDTO.id
            }

            if (!result.empty()) userPunishment = result.first().asUserPunishment()

            return@transaction userPunishment
        }
    }

    override fun create(createUserPunishmentDTO: CreateUserPunishmentDTO): UserPunishment {
        return transaction {
            return@transaction UserPunishmentDAO.new {
                this.userId = createUserPunishmentDTO.userId
                this.stafferId = createUserPunishmentDTO.stafferId
                this.punishType = createUserPunishmentDTO.punishType
                this.punishCategory = createUserPunishmentDTO.punishCategory
                this.duration = createUserPunishmentDTO.duration
                this.customReason = createUserPunishmentDTO.customReason
                this.proof = createUserPunishmentDTO.proof
                this.perpetual = createUserPunishmentDTO.perpetual
                this.hidden = createUserPunishmentDTO.hidden
            }.asUserPunishment()
        }
    }

    override fun update(updateUserPunishmentByIdDTO: UpdateUserPunishmentByIdDTO): Boolean {
        return transaction {
            val result = UserPunishmentDAO.find {
                UsersPunishmentsTable.id eq updateUserPunishmentByIdDTO.id
            }

            if (!result.empty()) updateUserPunishmentByIdDTO.execute.accept(result.first())

            return@transaction false
        }
    }

}