package com.redefantasy.core.spigot.misc.server.configuration.storage.repositories.implementations

import com.redefantasy.core.spigot.misc.server.configuration.data.ServerConfiguration
import com.redefantasy.core.spigot.misc.server.configuration.storage.dto.FetchServerConfigurationByServerNameDTO
import com.redefantasy.core.spigot.misc.server.configuration.storage.repositories.IServersConfigurationRepository
import com.redefantasy.core.spigot.misc.server.configuration.storage.table.ServersConfigurationsTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Gutyerrez
 */
class PostgresServersConfigurationRepository : IServersConfigurationRepository {

	override fun fetchServerConfigurationByServerName(
		fetchServerConfigurationByServerNameDTO: FetchServerConfigurationByServerNameDTO
	): ServerConfiguration? {
		return transaction {
			return@transaction ServersConfigurationsTable.select {
				ServersConfigurationsTable.server eq fetchServerConfigurationByServerNameDTO.serverName
			}.firstOrNull()?.get(ServersConfigurationsTable.configuration)
		}
	}

}