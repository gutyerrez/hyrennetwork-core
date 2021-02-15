package com.redefantasy.core.shared.users.groups.due.storage.dao

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.users.groups.due.storage.table.UsersGroupsDueTable
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

    val userId by UsersGroupsDueTable.userId
    val groupName by UsersGroupsDueTable.groupName
    val serverName by UsersGroupsDueTable.serverName

    fun server() = com.redefantasy.core.shared.CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
        this.serverName.value
    )

    fun group() = this.groupName

}