package net.hyren.core.shared.servers.storage.repositories

import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.servers.storage.dto.FetchServerByNameDTO
import net.hyren.core.shared.storage.repositories.IRepository
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
interface IServersRepository : IRepository {

    fun fetchAll(): Map<EntityID<String>, Server>

    fun fetchByName(
        fetchServerByNameDTO: FetchServerByNameDTO
    ): Server?

}