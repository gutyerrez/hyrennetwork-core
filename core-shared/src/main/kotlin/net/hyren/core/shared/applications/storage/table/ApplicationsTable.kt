package net.hyren.core.shared.applications.storage.table

import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.providers.databases.postgresql.dao.StringTable
import net.hyren.core.shared.servers.storage.table.ServersTable

/**
 * @author SrGutyerrez
 **/
object ApplicationsTable : StringTable("applications") {

    val displayName = varchar("display_name", 255)
    val slots = integer("slots")
    val address = varchar("address", 255)
    val port = integer("port")
    val applicationType = enumerationByName("application_type", 255, ApplicationType::class)
    val serverName = reference("server_name", ServersTable).nullable()
    val restrictJoinGroupName = enumerationByName("restrict_join_group_name", 255, Group::class)

}