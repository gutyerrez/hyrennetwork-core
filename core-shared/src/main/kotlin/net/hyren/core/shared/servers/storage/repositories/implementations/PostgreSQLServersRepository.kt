package net.hyren.core.shared.servers.storage.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.servers.storage.dao.ServerDAO
import net.hyren.core.shared.servers.storage.dto.FetchServerByNameDTO
import net.hyren.core.shared.servers.storage.repositories.IServersRepository
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgreSQLServersRepository : IServersRepository {

    override fun fetchAll() = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        val servers = mutableMapOf<EntityID<String>, Server>()

        ServerDAO.all().forEach { servers[it.name] = it.asServer() }

        servers
    }

    override fun fetchByName(
        fetchServerByNameDTO: FetchServerByNameDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        if (fetchServerByNameDTO.name === null) return@transaction null

        ServerDAO.findById(fetchServerByNameDTO.name)?.asServer()
    }

}