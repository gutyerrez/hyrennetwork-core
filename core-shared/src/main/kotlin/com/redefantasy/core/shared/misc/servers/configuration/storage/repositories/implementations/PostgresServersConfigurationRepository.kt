package com.redefantasy.core.shared.misc.servers.configuration.storage.repositories.implementations

import com.redefantasy.core.shared.misc.servers.configuration.data.ServerConfiguration
import com.redefantasy.core.shared.misc.servers.configuration.storage.dto.FetchServerConfigurationByServerNameDTO
import com.redefantasy.core.shared.misc.servers.configuration.storage.repositories.IServersConfigurationRepository
import com.redefantasy.core.shared.misc.servers.configuration.storage.table.ServersConfigurationTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Gutyerrez
 */
class PostgresServersConfigurationRepository : IServersConfigurationRepository<Any> {

	override fun fetchServerConfigurationByServerName(
		fetchServerConfigurationByServerNameDTO: FetchServerConfigurationByServerNameDTO
	): ServerConfiguration<Any>? {
		return transaction {
			return@transaction ServersConfigurationTable.select {
				ServersConfigurationTable.server eq fetchServerConfigurationByServerNameDTO.serverName
			}.firstOrNull()?.get(ServersConfigurationTable.configuration)
		}
	}

}