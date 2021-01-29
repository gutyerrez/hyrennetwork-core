package com.redefantasy.core.shared.servers.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.servers.data.Server

/**
 * @author SrGutyerrez
 **/
class ServersLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .build<String, Server>()

    fun fetchByName(name: String?): Server? {
        if (name === null) return null

        return this.CACHE.getIfPresent(name)
    }

    override fun populate() {
        this.CACHE.putAll(
                CoreProvider.Repositories.Postgres.SERVERS_REPOSITORY.provide().fetchAll()
        )
    }

}