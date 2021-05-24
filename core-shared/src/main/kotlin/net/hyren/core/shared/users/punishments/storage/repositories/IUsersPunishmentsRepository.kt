package net.hyren.core.shared.users.punishments.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.punishments.data.UserPunishment
import net.hyren.core.shared.users.punishments.storage.dto.CreateUserPunishmentDTO
import net.hyren.core.shared.users.punishments.storage.dto.FetchUserPunishmentByIdDTO
import net.hyren.core.shared.users.punishments.storage.dto.FetchUserPunishmentsByUserIdDTO
import net.hyren.core.shared.users.punishments.storage.dto.UpdateUserPunishmentByIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersPunishmentsRepository : IRepository {

    fun fetchByUserId(
        fetchUserPunishmentsByUserIdDTO: FetchUserPunishmentsByUserIdDTO
    ): List<UserPunishment>

    fun fetchById(
        fetchUserPunishmentsByIdDTO: FetchUserPunishmentByIdDTO
    ): UserPunishment?

    fun create(
        createUserPunishmentDTO: CreateUserPunishmentDTO
    ): UserPunishment?

    fun update(
        updateUserPunishmentByIdDTO: UpdateUserPunishmentByIdDTO
    ): Boolean

}