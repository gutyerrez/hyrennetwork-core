package com.redefantasy.core.shared.users.punishments.storage.dao

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.users.punishments.data.UserPunishment
import com.redefantasy.core.shared.users.punishments.storage.table.UsersPunishmentsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class UserPunishmentDAO(
        id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<UserPunishmentDAO>(UsersPunishmentsTable)

    var userId by UsersPunishmentsTable.userId
    var stafferId by UsersPunishmentsTable.stafferId
    var startTime by UsersPunishmentsTable.startTime
    var punishType by UsersPunishmentsTable.punishType
    var punishCategory by UsersPunishmentsTable.punishCategory
    var duration by UsersPunishmentsTable.duration
    var customReason by UsersPunishmentsTable.customReason
    var proof by UsersPunishmentsTable.proof
    var revokeStafferId by UsersPunishmentsTable.revokeStafferId
    var revokeTime by UsersPunishmentsTable.revokeTime
    var revokeCategory by UsersPunishmentsTable.revokeCategory
    var hidden by UsersPunishmentsTable.hidden
    var perpetual by UsersPunishmentsTable.perpetual
    var createdAt by UsersPunishmentsTable.createdAt
    var updatedAt by UsersPunishmentsTable.updatedAt

    fun asUserPunishment() = UserPunishment(
            this.id.value,
            this.userId,
            this.stafferId,
            this.startTime,
            this.punishType,
            com.redefantasy.core.shared.CoreProvider.Cache.Local.PUNISH_CATEGORIES.provide().fetchByName(
                    this.punishCategory?.value ?: ""
            ),
            this.duration,
            this.customReason,
            this.proof,
            this.revokeStafferId,
            this.revokeTime,
            com.redefantasy.core.shared.CoreProvider.Cache.Local.REVOKE_CATEGORIES.provide().fetchByName(
                    this.punishCategory?.value ?: ""
            ),
            this.hidden,
            this.perpetual,
            this.createdAt,
            this.updatedAt
    )

}