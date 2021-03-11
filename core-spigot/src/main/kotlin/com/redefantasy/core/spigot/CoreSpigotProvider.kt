package com.redefantasy.core.spigot

import com.redefantasy.core.shared.providers.databases.postgres.providers.PostgresRepositoryProvider
import com.redefantasy.core.spigot.misc.spawn.repositories.ISpawnRepository
import com.redefantasy.core.spigot.misc.spawn.repositories.implementations.PostgresSpawnRepository

/**
 * @author Gutyerrez
 */
object CoreSpigotProvider {

    fun prepare() {
        Repositories.Postgres.SPAWN_REPOSITORY.prepare()
    }

    object Repositories {

        object Postgres {

            val SPAWN_REPOSITORY = PostgresRepositoryProvider<ISpawnRepository>(
                PostgresSpawnRepository::class
            )

        }

    }

}