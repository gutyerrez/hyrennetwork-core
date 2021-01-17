package com.redefantasy.core.shared.users.groups.due.storage.table

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.servers.storage.table.ServersTable
import com.redefantasy.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author SrGutyerrez
 **/
object UsersGroupsDueTable : IntIdTable("users_groups_due") {

    val userId = reference("user_id", UsersTable)
    val groupName = enumerationByName("group_name", 255, Group::class)
    val serverName = reference("server_name", ServersTable)
    val dueAt = datetime("due_at")

}