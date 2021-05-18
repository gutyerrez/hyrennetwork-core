package net.hyren.core.spigot.misc.server.configuration.storage.repositories.implementations

import net.hyren.core.spigot.misc.server.configuration.data.ServerConfiguration
import net.hyren.core.spigot.misc.server.configuration.storage.dto.FetchServerConfigurationByServerNameDTO
import net.hyren.core.spigot.misc.server.configuration.storage.repositories.IServersConfigurationRepository
import net.hyren.core.spigot.misc.server.configuration.storage.table.ServersConfigurationsTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Gutyerrez
 */
class PostgreSQLServersConfigurationRepository : IServersConfigurationRepository {

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