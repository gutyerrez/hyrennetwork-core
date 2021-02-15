package com.redefantasy.core.shared.users.passwords.storage.table

import com.redefantasy.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author Gutyerrez
 */
object UserPasswordTable : IntIdTable("users_passwords") {

    val userId = entityId("user_id", UsersTable)
    val password = varchar("password", 255)
    val enabled = bool("enabled")
    val createdAt = datetime("created_at")

}