package net.hyren.core.shared.servers.storage.dao

import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntity
import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntityClass
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.servers.storage.table.ServersTable
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