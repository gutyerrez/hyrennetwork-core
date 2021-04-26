package com.redefantasy.core.shared.misc.server.configuration.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.misc.server.configuration.data.ServerConfiguration
import com.redefantasy.core.shared.misc.server.configuration.storage.dto.FetchServerConfigurationByServerNameDTO
import com.redefantasy.core.shared.servers.data.Server
import org.jetbrains.exposed.dao.id.EntityID
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class ServersConfigurationsLocalCache<T> : LocalCache {

	private val CACHE = Caffeine.newBuilder()
		.expireAfterWrite(10, TimeUnit.SECONDS)
		.build<Server, ServerConfiguration<T>?> {
			CoreProvider.Repositories.Postgres.SERVERS_CONFIGURATION_REPOSITORY.provide().fetchServerConfigurationByServerName(
				FetchServerConfigurationByServerNameDTO(
					it.name
				)
			) as? ServerConfiguration<T>
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