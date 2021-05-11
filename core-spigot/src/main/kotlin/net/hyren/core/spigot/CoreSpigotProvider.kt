package net.hyren.core.spigot

import net.hyren.core.shared.providers.cache.local.LocalCacheProvider
import net.hyren.core.shared.providers.databases.mariadb.providers.MariaDBRepositoryProvider
import net.hyren.core.spigot.misc.server.configuration.cache.local.ServersConfigurationsLocalCache
import net.hyren.core.spigot.misc.server.configuration.storage.repositories.IServersConfigurationRepository
import net.hyren.core.spigot.misc.server.configuration.storage.repositories.implementations.MariaDBServersConfigurationRepository

/**
 * @author Gutyerrez
 */
object CoreSpigotProvider {

    fun prepare() {
        Repositories.MariaDB.SERVERS_CONFIGURATION_REPOSITORY.prepare()

        Cache.Local.SERVER_CONFIGURATION.prepare()
    }

    object Repositories {

        object MariaDB {

            val SERVERS_CONFIGURATION_REPOSITORY = MariaDBRepositoryProvider<IServersConfigurationRepository>(
                MariaDBServersConfigurationRepository::class
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