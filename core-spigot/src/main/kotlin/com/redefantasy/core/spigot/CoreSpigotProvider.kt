package com.redefantasy.core.spigot

import com.redefantasy.core.shared.providers.cache.local.LocalCacheProvider
import com.redefantasy.core.shared.providers.databases.postgres.providers.PostgresRepositoryProvider
import com.redefantasy.core.spigot.misc.server.configuration.cache.local.ServersConfigurationsLocalCache
import com.redefantasy.core.spigot.misc.server.configuration.storage.repositories.IServersConfigurationRepository
import com.redefantasy.core.spigot.misc.server.configuration.storage.repositories.implementations.PostgresServersConfigurationRepository

/**
 * @author Gutyerrez
 */
object CoreSpigotProvider {

    fun prepare() {
        Repositories.Postgres.SERVERS_CONFIGURATION_REPOSITORY.prepare()

        Cache.Local.SERVER_CONFIGURATION.prepare()
    }

    object Repositories {

        object Postgres {

            val SERVERS_CONFIGURATION_REPOSITORY = PostgresRepositoryProvider<IServersConfigurationRepository>(
                PostgresServersConfigurationRepository::class
            )

        }

    }

    object Cache {

        object Local {

            val SERVER_CONFIGURATION = LocalCacheProvider(
                ServersConfigurationsLocalCache()
            )

        }
    }

}