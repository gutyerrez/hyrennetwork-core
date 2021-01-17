package com.redefantasy.core.shared.misc.report.category.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.misc.report.category.data.ReportCategory

/**
 * @author SrGutyerrez
 **/
class ReportCategoriesLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .build<String, ReportCategory>()

    override fun populate() {
        this.CACHE.putAll(
                CoreProvider.Repositories.Postgres.REPORT_CATEGORIES_REPOSITORY.provide().fetchAll()
        )
    }

}