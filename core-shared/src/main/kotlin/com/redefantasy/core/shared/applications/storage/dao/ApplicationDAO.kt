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

    fun asApplication() = Application(
        this.name.value,
        this.displayName,
        this.slots,
        InetSocketAddress(
            this.address,
            this.port,
        ),
        this.applicationType,
        CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
            this.serverName?.value
        ) ?: CoreProvider.Repositories.Postgres.SERVERS_REPOSITORY.provide().fetchByName(
            FetchServerByNameDTO(
                this.serverName?.value
            )
        ),
        this.restrictJoinGroupName
    )

}