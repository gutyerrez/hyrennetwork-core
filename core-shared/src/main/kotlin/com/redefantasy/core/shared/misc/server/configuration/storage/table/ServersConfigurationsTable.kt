package com.redefantasy.core.shared.misc.server.configuration.storage.table

import com.redefantasy.core.shared.misc.exposed.json
import com.redefantasy.core.shared.misc.server.configuration.data.ServerConfiguration
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringTable
import com.redefantasy.core.shared.servers.storage.table.ServersTable

/**
 * @author Gutyerrez
 */
object ServersConfigurationsTable : StringTable("servers_configurations") {

	val server = reference("server", ServersTable)
	val configuration = json<ServerConfiguration<Any>>(
		"configuration",
		ServerConfiguration::class
	)

}