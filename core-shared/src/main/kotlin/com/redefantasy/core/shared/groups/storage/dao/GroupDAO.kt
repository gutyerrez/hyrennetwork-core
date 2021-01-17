package com.redefantasy.core.shared.groups.storage.dao

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.groups.storage.table.GroupsTable
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntity
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class GroupDAO(
        name: EntityID<String>
) : StringEntity(name) {

    companion object : StringEntityClass<GroupDAO>(GroupsTable)

    val displayName by GroupsTable.displayName
    val prefix by GroupsTable.prefix
    val suffix by GroupsTable.suffix
    val color by GroupsTable.color
    val priority by GroupsTable.priority
    val tabListOrder by GroupsTable.tabListOrder
    val discordRoleId by GroupsTable.discordRoleId

    fun asGroup(): Group {
        val group = Group.valueOf(this.id.value)

        group.displayName = this.displayName
        group.prefix = this.prefix
        group.suffix = this.suffix
        group.color = this.color
        group.priority = this.priority
        group.tabListOrder = this.tabListOrder
        group.discordRoleId = this.discordRoleId

        return group
    }

}