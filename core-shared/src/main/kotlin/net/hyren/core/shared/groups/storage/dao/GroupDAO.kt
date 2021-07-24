package net.hyren.core.shared.groups.storage.dao

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.groups.storage.table.GroupsTable
import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntity
import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntityClass
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
    val priority by GroupsTable.priority
    val discordRoleId by GroupsTable.discordRoleId

    fun readGroup(): Group {
        val group = Group.valueOf(id.value)

        println(prefix)

        group.displayName = displayName
        group.prefix = prefix
        group.suffix = suffix
        group.priority = priority
        group.discordRoleId = discordRoleId

        return group
    }

}