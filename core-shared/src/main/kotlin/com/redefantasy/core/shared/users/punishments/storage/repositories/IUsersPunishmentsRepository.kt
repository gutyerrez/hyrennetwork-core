package com.redefantasy.core.shared.users.punishments.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.punishments.data.UserPunishment
import com.redefantasy.core.shared.users.punishments.storage.dto.CreateUserPunishmentDTO
import com.redefantasy.core.shared.users.punishments.storage.dto.FetchUserPunishmentByIdDTO
import com.redefantasy.core.shared.users.punishments.storage.dto.FetchUserPunishmentsByUserIdDTO
import com.redefantasy.core.shared.users.punishments.storage.dto.UpdateUserPunishmentByIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersPunishmentsRepository : IRepository {

    fun fetchByUserId(fetchUserPunishmentsByUserIdDTO: FetchUserPunishmentsByUserIdDTO): List<UserPunishment>

    fun fetchById(fetchUserPunishmentsByIdDTO: FetchUserPunishmentByIdDTO): UserPunishment?

    fun create(createUserPunishmentDTO: CreateUserPunishmentDTO): UserPunishment?

    fun update(updateUserPunishmentByIdDTO: UpdateUserPunishmentByIdDTO): Boolean

}