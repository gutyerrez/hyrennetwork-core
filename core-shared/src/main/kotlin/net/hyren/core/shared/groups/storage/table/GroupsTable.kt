package net.hyren.core.shared.groups.storage.table

import net.hyren.core.shared.misc.exposed.array
import net.hyren.core.shared.providers.databases.postgresql.dao.StringTable
import net.md_5.bungee.api.chat.BaseComponent

/**
 * @author SrGutyerrez
 **/
object GroupsTable : StringTable("groups") {

    val displayName = varchar("display_name", 255)
    val prefix = array<BaseComponent>("prefix", Array<BaseComponent>::class)
    val suffix = array<BaseComponent>("suffix", Array<BaseComponent>::class).nullable()
    val priority = integer("priority")
    val discordRoleId = long("discord_role_id")

}