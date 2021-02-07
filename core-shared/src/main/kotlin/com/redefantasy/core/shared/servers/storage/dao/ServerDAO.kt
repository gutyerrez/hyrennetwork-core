package com.redefantasy.core.shared.servers.storage.dao

import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntity
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntityClass
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.servers.storage.table.ServersTable
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class ServerDAO(
        val name: EntityID<String>
) : StringEntity(name) {

    companion object : StringEntityClass<ServerDAO>(ServersTable)

    val displayName by ServersTable.displayName
    val serverType by ServersTable.serverType

    fun asServer(): Server = Server(
            this.name,
            this.displayName,
            this.serverType
    )

}