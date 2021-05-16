package net.hyren.core.spigot.misc.server.configuration.storage.table

import net.hyren.core.shared.misc.exposed.json
import net.hyren.core.shared.servers.storage.table.ServersTable
import net.hyren.core.spigot.misc.server.configuration.data.ServerConfiguration
import org.jetbrains.exposed.sql.Table

/**
 * @author Gutyerrez
 */
object ServersConfigurationsTable : Table("servers_configurations") {

	val server = reference("name", ServersTable)
	val configuration = json<ServerConfiguration>("configuration", ServerConfiguration::class)

}