package com.redefantasy.core.shared.servers.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.common.base.Strings
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.servers.storage.dto.FetchServerByNameDTO
import com.redefantasy.core.shared.servers.storage.table.ServersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class ServersLocalCache : LocalCache {

    private val CACHE_BY_NAME = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<EntityID<String>, Server> {
            CoreProvider.Repositories.Postgres.SERVERS_REPOSITORY.provide().fetchByName(
                FetchServerByNameDTO(
                    it
                )
            )
        }

    private val CACHE_BY_ALL = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<Any, Array<Server>> {
            CoreProvider.Repositories.Postgres.SERVERS_REPOSITORY.provide().fetchAll().values.toTypedArray()
        }

    fun fetchAll() = this.CACHE_BY_ALL.get(
        Strings.nullToEmpty(null)
    ) ?: emptyArray()

    fun fetchByName(name: EntityID<String>?): Server? {
        if (name === null) return null

        println(this.CACHE_BY_NAME.asMap().size)

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