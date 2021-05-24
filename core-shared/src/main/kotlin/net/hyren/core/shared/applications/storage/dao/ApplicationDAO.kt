package net.hyren.core.shared.applications.storage.dao

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.storage.table.ApplicationsTable
import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntity
import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntityClass
import net.hyren.core.shared.servers.storage.dto.FetchServerByNameDTO
import org.jetbrains.exposed.dao.id.EntityID
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
class ApplicationDAO(
    val name: EntityID<String>
) : StringEntity(name) {

    companion object : StringEntityClass<ApplicationDAO>(ApplicationsTable)

    val displayName by ApplicationsTable.displayName
    val slots by ApplicationsTable.slots
    val address by ApplicationsTable.address
    val port by ApplicationsTable.port
    val applicationType by ApplicationsTable.applicationType
    val serverName by ApplicationsTable.serverName
    val restrictJoinGroupName by ApplicationsTable.restrictJoinGroupName

    fun toApplication() = Application(
        name.value,
        displayName,
        slots,
        InetSocketAddress(
            address,
            port,
        ),
        applicationType,
        CoreProvider.Repositories.PostgreSQL.SERVERS_REPOSITORY.provide().fetchByName(
            FetchServerByNameDTO(
                serverName
            )
        ) ?: CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
            serverName?.value
        ),
        restrictJoinGroupName
    )

}