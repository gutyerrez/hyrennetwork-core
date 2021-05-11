package net.hyren.core.shared.servers.storage.table

import net.hyren.core.shared.providers.databases.mariadb.dao.StringTable
import net.hyren.core.shared.servers.ServerType

/**
 * @author SrGutyerrez
 **/
object ServersTable : StringTable("servers") {

    val displayName = varchar("display_name", 255)
    val serverType = enumerationByName("server_type", 255, ServerType::class)

}