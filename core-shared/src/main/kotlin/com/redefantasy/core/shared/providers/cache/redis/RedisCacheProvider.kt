package com.redefantasy.core.shared.providers.cache.redis

import com.redefantasy.core.shared.cache.redis.RedisCache
import com.redefantasy.core.shared.providers.IProvider

/**
 * @author SrGutyerrez
 **/
class RedisCacheProvider<T: RedisCache>(
        private val t: T
) : IProvider<T> {

    override fun prepare() = this.t.populate()

    override fun provide() = this.t

}