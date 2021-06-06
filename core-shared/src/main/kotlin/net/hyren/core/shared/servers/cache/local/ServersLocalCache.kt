package net.hyren.core.shared.servers.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.misc.caffeine.get
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.servers.storage.dto.FetchServerByNameDTO
import net.hyren.core.shared.servers.storage.table.ServersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class ServersLocalCache : LocalCache {

    private val CACHE_BY_NAME = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<EntityID<String>, Server> {
            CoreProvider.Repositories.PostgreSQL.SERVERS_REPOSITORY.provide().fetchByName(
                FetchServerByNameDTO(
                    it
                )
            )
        }

    private val CACHE_BY_ALL = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<Any, Array<Server>> {
            CoreProvider.Repositories.PostgreSQL.SERVERS_REPOSITORY.provide().fetchAll().values.toTypedArray()
        }

    fun fetchAll() = this.CACHE_BY_ALL.get() ?: emptyArray()

    fun fetchByName(name: EntityID<String>?): Server? {
        if (name === null) return null

        return this.CACHE_BY_NAME.get(name)
    }

    fun fetchByName(name: String?): Server? {
        if (name === null) return null

        return this.CACHE_BY_NAME.get(
            EntityID(
                name,
                ServersTable
            )
        )
    }

}