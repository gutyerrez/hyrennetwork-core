package com.redefantasy.core.spigot.misc.spawn.repositories.implementations

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.world.location.SerializedLocation
import com.redefantasy.core.spigot.misc.spawn.repositories.ISpawnRepository
import com.redefantasy.core.spigot.misc.spawn.repositories.table.SpawnTable
import com.redefantasy.core.spigot.misc.spawn.repositories.table.SpawnTable.asSerializedLocation
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Gutyerrez
 */
class PostgresSpawnRepository : ISpawnRepository {

    override fun fetch(): SerializedLocation? = transaction {
        val result = SpawnTable.select {
            SpawnTable.applicationName eq CoreProvider.application.name
        }

        if (result.empty()) return@transaction null

        return@transaction result.first().asSerializedLocation()
    }

}