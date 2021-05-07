package net.hyren.core.shared.providers.cache.redis

import net.hyren.core.shared.cache.redis.RedisCache
import net.hyren.core.shared.providers.IProvider

/**
 * @author SrGutyerrez
 **/
class RedisCacheProvider<T: RedisCache>(
        private val t: T
) : IProvider<T> {

    override fun prepare() = this.t.populate()

    override fun provide() = this.t

}