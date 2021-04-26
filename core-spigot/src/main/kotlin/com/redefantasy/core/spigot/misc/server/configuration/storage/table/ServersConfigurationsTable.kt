package com.redefantasy.core.spigot.misc.server.configuration.storage.table

import com.redefantasy.core.shared.misc.exposed.json
import com.redefantasy.core.shared.servers.storage.table.ServersTable
import com.redefantasy.core.spigot.misc.server.configuration.data.ServerConfiguration
import org.jetbrains.exposed.sql.Table

/**
 * @author Gutyerrez
 */
object ServersConfigurationsTable : Table("servers_configurations") {

	val server = reference("name", ServersTable)
	val configuration = json<ServerConfiguration>(
		"configuration",
		ServerConfiguration::class
	)

}