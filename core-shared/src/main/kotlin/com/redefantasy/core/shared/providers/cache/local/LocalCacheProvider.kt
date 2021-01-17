package com.redefantasy.core.shared.providers.cache.local

import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.providers.IProvider

/**
 * @author SrGutyerrez
 **/
class LocalCacheProvider<T: LocalCache>(
        private val t: T
) : IProvider<T> {

    override fun prepare() = this.t.populate()

    override fun provide() = this.t

}