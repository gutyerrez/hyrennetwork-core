package net.hyren.core.shared.users.groups.due.storage.table

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.storage.table.ServersTable
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author SrGutyerrez
 **/
object UsersGroupsDueTable : IntIdTable("users_groups_due") {

    val userId = reference("user_id", UsersTable)
    val group = enumerationByName("group_name", 255, Group::class)
    val serverName = reference("server_name", ServersTable).nullable()
    val dueAt = datetime("due_at")

}