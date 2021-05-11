package net.hyren.core.spigot.misc.server.configuration.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.spigot.CoreSpigotProvider
import net.hyren.core.spigot.misc.server.configuration.data.ServerConfiguration
import net.hyren.core.spigot.misc.server.configuration.storage.dto.FetchServerConfigurationByServerNameDTO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class ServersConfigurationsLocalCache : LocalCache {

	private val CACHE = Caffeine.newBuilder()
		.expireAfterWrite(10, TimeUnit.SECONDS)
		.build<Server, ServerConfiguration?> {
			CoreSpigotProvider.Repositories.MariaDB.SERVERS_CONFIGURATION_REPOSITORY.provide().fetchServerConfigurationByServerName(
				FetchServerConfigurationByServerNameDTO(
					it.name
				)
			)
		}

	fun fetchByServer(
		server: Server?
	) = this.CACHE.get(
		server ?: throw IllegalArgumentException("Server must be not null")
	)

	fun fetchByServerName(
		serverName: EntityID<String>
	) = this.CACHE.get(
		CoreProvider.Cache.Local.SERVERS.provide().fetchByName(serverName) ?: throw IllegalArgumentException("Server name must be not null")
	)

	fun fetchByServerName(
		serverName: String
	) = this.CACHE.get(
		CoreProvider.Cache.Local.SERVERS.provide().fetchByName(serverName) ?: throw IllegalArgumentException("Server name must be not null")
	)

}