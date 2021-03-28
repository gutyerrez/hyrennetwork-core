package com.redefantasy.core.shared.servers.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.servers.storage.dto.FetchServerByNameDTO
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class ServersLocalCache : LocalCache {

    private val CACHE_BY_NAME = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<String, Server> {
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

    fun fetchAll() = this.CACHE_BY_ALL.asMap().values

    fun fetchByName(name: String?): Server? {
        if (name === null) return null

        return this.CACHE_BY_NAME.getIfPresent(name)
    }

}