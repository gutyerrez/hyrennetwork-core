package net.hyren.core.shared.misc.report.category.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.misc.report.category.data.ReportCategory

/**
 * @author SrGutyerrez
 **/
class ReportCategoriesLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .build<String, ReportCategory>()

    override fun populate() {
        this.CACHE.putAll(
                CoreProvider.Repositories.PostgreSQL.REPORT_CATEGORIES_REPOSITORY.provide().fetchAll()
        )
    }

}