package com.redefantasy.core.shared.servers.storage.repositories

import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IServersRepository : IRepository {

    fun fetchAll(): Map<String, Server>

}