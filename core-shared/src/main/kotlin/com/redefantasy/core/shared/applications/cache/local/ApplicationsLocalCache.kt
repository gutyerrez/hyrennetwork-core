package com.redefantasy.core.shared.applications.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.cache.local.LocalCache

/**
 * @author SrGutyerrez
 **/
class ApplicationsLocalCache : LocalCache {

    private val CACHE = Caffeine
            .newBuilder()
            .build<String, Application>()

    fun fetchByName(name: String) = this.CACHE.getIfPresent(name)

    override fun populate() {
        super.populate()
    }

}