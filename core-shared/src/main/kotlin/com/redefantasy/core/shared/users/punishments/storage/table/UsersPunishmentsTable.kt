package com.redefantasy.core.shared.users.punishments.storage.table

import com.redefantasy.core.shared.misc.punish.PunishType
import com.redefantasy.core.shared.misc.punish.category.storage.table.PunishCategoriesTable
import com.redefantasy.core.shared.misc.revoke.category.storage.table.RevokeCategoriesTable
import com.redefantasy.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author SrGutyerrez
 **/
object UsersPunishmentsTable : IntIdTable("users_punishments") {

    val userId = reference("user_id", UsersTable)
    val stafferId = reference("staffer_id", UsersTable)
    val startTime = datetime("start_time").nullable()
    val punishType = enumerationByName("punish_type", 255, PunishType::class)
    val punishCategory = reference("punish_category", PunishCategoriesTable).nullable()
    val duration = long("duration")
    val customReason = varchar("custom_reason", 255).nullable()
    val proof = varchar("proof", 255).nullable()
    val revokeStafferId = reference("revoke_staffer_id", UsersTable).nullable()
    val revokeTime = datetime("revoke_time").nullable()
    val revokeCategory = reference("revoke_category", RevokeCategoriesTable).nullable()
    val hidden = bool("hidden")
    val perpetual = bool("perpetual")
    val createdAt = datetime("created_at").nullable()
    val updatedAt = datetime("updated_at").nullable()

}