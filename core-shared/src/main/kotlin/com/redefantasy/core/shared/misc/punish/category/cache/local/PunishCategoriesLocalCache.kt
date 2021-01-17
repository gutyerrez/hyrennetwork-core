package com.redefantasy.core.shared.misc.punish.category.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.misc.punish.category.data.PunishCategory

/**
 * @author SrGutyerrez
 **/
class PunishCategoriesLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .build<String, PunishCategory>()

    fun fetchByName(name: String) = this.CACHE.getIfPresent(name)

    override fun populate() {
        this.CACHE.putAll(
                CoreProvider.Repositories.Postgres.PUNISH_CATEGORIES_REPOSITORY.provide().fetchAll()
        )
    }

}