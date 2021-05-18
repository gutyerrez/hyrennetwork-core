package net.hyren.core.shared.misc.punish.category.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.misc.punish.category.data.PunishCategory

/**
 * @author SrGutyerrez
 **/
class PunishCategoriesLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .build<String, PunishCategory>()

    fun fetchAll() = this.CACHE.asMap().values

    fun fetchByName(name: String) = this.CACHE.getIfPresent(name)

    override fun populate() {
        this.CACHE.putAll(
                CoreProvider.Repositories.PostgreSQL.PUNISH_CATEGORIES_REPOSITORY.provide().fetchAll()
        )
    }

}