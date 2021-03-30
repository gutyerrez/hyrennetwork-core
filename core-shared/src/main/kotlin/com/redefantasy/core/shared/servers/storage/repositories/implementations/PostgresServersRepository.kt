package com.redefantasy.core.shared.servers.storage.repositories.implementations

import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.servers.storage.dao.ServerDAO
import com.redefantasy.core.shared.servers.storage.dto.FetchServerByNameDTO
import com.redefantasy.core.shared.servers.storage.repositories.IServersRepository
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresServersRepository : IServersRepository {

    override fun fetchAll(): Map<EntityID<String>, Server> {
        return transaction {
            val servers = mutableMapOf<EntityID<String>, Server>()

            ServerDAO.all().forEach { servers[it.name] = it.asServer() }

            println("Transaction: ${servers.size}")

            return@transaction servers
        }
    }

    override fun fetchByName(
        fetchServerByNameDTO: FetchServerByNameDTO
    ): Server? {
        return transaction {
            if (fetchServerByNameDTO.name === null) return@transaction null

            return@transaction ServerDAO.findById(
                fetchServerByNameDTO.name
            )?.asServer()
        }
    }

}