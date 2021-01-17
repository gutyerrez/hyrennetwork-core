package com.redefantasy.core.shared.servers.storage.repositories.implementations

import com.google.common.collect.Maps
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.servers.storage.dao.ServerDAO
import com.redefantasy.core.shared.servers.storage.repositories.IServersRepository
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresServersRepository : IServersRepository {

    override fun fetchAll(): Map<String, Server> {
        return transaction {
            val servers = Maps.newConcurrentMap<String, Server>()

            ServerDAO.all().forEach {
                servers[it.name.value] = it.asServer()
            }

            return@transaction servers
        }
    }

}