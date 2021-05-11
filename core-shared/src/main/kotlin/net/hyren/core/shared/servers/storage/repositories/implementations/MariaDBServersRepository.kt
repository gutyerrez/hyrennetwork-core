package net.hyren.core.shared.servers.storage.repositories.implementations

import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.servers.storage.dao.ServerDAO
import net.hyren.core.shared.servers.storage.dto.FetchServerByNameDTO
import net.hyren.core.shared.servers.storage.repositories.IServersRepository
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class MariaDBServersRepository : IServersRepository {

    override fun fetchAll(): Map<EntityID<String>, Server> {
        return transaction {
            val servers = mutableMapOf<EntityID<String>, Server>()

            ServerDAO.all().forEach { servers[it.name] = it.asServer() }

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