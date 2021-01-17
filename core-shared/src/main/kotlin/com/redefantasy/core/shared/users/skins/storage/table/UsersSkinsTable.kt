package com.redefantasy.core.shared.users.skins.storage.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author SrGutyerrez
 **/
object UsersSkinsTable : IntIdTable("users_skins") {

    val name = varchar("name", 255).uniqueIndex()
    val value = varchar("value", 255)
    val signature = varchar("signature", 255)
    val updatedAt = datetime("updated_at")

}