package net.hyren.core.shared.users.skins.storage.table

import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author SrGutyerrez
 **/
object UsersSkinsTable : IntIdTable("users_skins") {

    val name = varchar("name", 255).uniqueIndex()
    val userId = reference("user_id", UsersTable)
    val value = text("value")
    val signature = text("signature")
    var enabled = bool("enabled")
    var updatedAt = datetime("updated_at")

}