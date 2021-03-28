package com.redefantasy.core.shared.applications.storage.dao

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.storage.table.ApplicationsTable
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntity
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntityClass
import com.redefantasy.core.shared.servers.storage.dto.FetchServerByNameDTO
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

    fun asApplication(): Application {
        println(serverName?.value)

        return Application(
            name.value,
            displayName,
            slots,
            InetSocketAddress(
                address,
                port,
            ),
            applicationType,
            CoreProvider.Repositories.Postgres.SERVERS_REPOSITORY.provide().fetchByName(
                FetchServerByNameDTO(
                    serverName?.value
                )
            ) ?: CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
                serverName?.value
            ),
            restrictJoinGroupName
        )
    }

}