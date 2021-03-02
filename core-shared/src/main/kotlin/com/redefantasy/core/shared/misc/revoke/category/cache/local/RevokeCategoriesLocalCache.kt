package com.redefantasy.core.shared.misc.revoke.category.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.misc.revoke.category.data.RevokeCategory

/**
 * @author SrGutyerrez
 **/
class RevokeCategoriesLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .build<String, RevokeCategory>()

    fun fetchAll() = this.CACHE.asMap().values

    fun fetchByName(name: String) = this.CACHE.getIfPresent(name)

    override fun populate() {
        this.CACHE.putAll(
                CoreProvider.Repositories.Postgres.REVOKE_CATEGORIES_REPOSITORY.provide().fetchAll()
        )
    }

}