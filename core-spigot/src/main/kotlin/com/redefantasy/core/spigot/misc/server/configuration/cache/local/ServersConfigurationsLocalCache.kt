package com.redefantasy.core.spigot.misc.server.configuration.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.spigot.CoreSpigotProvider
import com.redefantasy.core.spigot.misc.server.configuration.data.ServerConfiguration
import com.redefantasy.core.spigot.misc.server.configuration.storage.dto.FetchServerConfigurationByServerNameDTO
import org.jetbrains.exposed.dao.id.EntityID
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class ServersConfigurationsLocalCache : LocalCache {

	private val CACHE = Caffeine.newBuilder()
		.expireAfterWrite(10, TimeUnit.SECONDS)
		.build<Server, ServerConfiguration?> {
			CoreSpigotProvider.Repositories.Postgres.SERVERS_CONFIGURATION_REPOSITORY.provide().fetchServerConfigurationByServerName(
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