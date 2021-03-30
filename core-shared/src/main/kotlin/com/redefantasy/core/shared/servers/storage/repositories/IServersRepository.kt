package com.redefantasy.core.shared.servers.storage.repositories

import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.servers.storage.dto.FetchServerByNameDTO
import com.redefantasy.core.shared.storage.repositories.IRepository
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