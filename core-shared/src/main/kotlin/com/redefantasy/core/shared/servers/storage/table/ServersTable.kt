package com.redefantasy.core.shared.servers.storage.table

import com.redefantasy.core.shared.providers.databases.postgres.dao.StringTable
import com.redefantasy.core.shared.servers.ServerType

/**
 * @author SrGutyerrez
 **/
object ServersTable : StringTable("servers") {

    val displayName = varchar("display_name", 255)
    val serverType = enumerationByName("server_type", 255, ServerType::class)

}