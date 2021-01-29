package com.redefantasy.core.shared.applications.storage.table

import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringTable
import com.redefantasy.core.shared.servers.storage.table.ServersTable

/**
 * @author SrGutyerrez
 **/
object ApplicationsTable : StringTable("applications") {

    val displayName = varchar("display_name", 255)
    val description = varchar("description", 255)
    val slots = integer("slots")
    val address = varchar("address", 255)
    val port = integer("port")
    val applicationType = enumerationByName("application_type", 255, ApplicationType::class)
    val serverName = reference("server_name", ServersTable).nullable()
    val restrictJoinGroupName = enumerationByName("restrict_join_group_name", 255, Group::class)

}