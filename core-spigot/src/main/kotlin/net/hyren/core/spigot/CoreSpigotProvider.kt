package net.hyren.core.spigot

import net.hyren.core.shared.providers.cache.local.LocalCacheProvider
import net.hyren.core.shared.providers.databases.postgres.providers.PostgresRepositoryProvider
import net.hyren.core.spigot.misc.server.configuration.cache.local.ServersConfigurationsLocalCache
import net.hyren.core.spigot.misc.server.configuration.storage.repositories.IServersConfigurationRepository
import net.hyren.core.spigot.misc.server.configuration.storage.repositories.implementations.PostgresServersConfigurationRepository

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