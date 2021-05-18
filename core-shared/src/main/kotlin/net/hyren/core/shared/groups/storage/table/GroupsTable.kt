package net.hyren.core.shared.groups.storage.table

import net.hyren.core.shared.providers.databases.postgresql.dao.StringTable

/**
 * @author SrGutyerrez
 **/
object GroupsTable : StringTable("groups") {

    val displayName = varchar("display_name", 255)
    val prefix = varchar("prefix", 32)
    val suffix = varchar("suffix", 32).nullable()
    val color = varchar("color", 7)
    val priority = integer("priority")
    val discordRoleId = long("discord_role_id").nullable()

}