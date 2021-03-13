package com.redefantasy.core.shared.groups.storage.table

import com.redefantasy.core.shared.providers.databases.postgres.dao.StringTable

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