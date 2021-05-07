package net.hyren.core.shared.users.groups.due.storage.dao

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.users.groups.due.storage.table.UsersGroupsDueTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class UserGroupDueDAO(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<UserGroupDueDAO>(UsersGroupsDueTable)

    var userId by UsersGroupsDueTable.userId
    var group by UsersGroupsDueTable.group
    var serverName by UsersGroupsDueTable.serverName
    var dueAt by UsersGroupsDueTable.dueAt

    fun server() = CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
        this.serverName?.value
    )

}