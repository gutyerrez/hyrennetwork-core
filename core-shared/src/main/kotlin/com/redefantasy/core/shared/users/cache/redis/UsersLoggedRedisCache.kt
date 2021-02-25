package com.redefantasy.core.shared.users.cache.redis

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.redis.RedisCache
import com.redefantasy.core.shared.users.data.User
import java.util.*

/**
 * @author Gutyerrez
 */
class UsersLoggedRedisCache : RedisCache {

    fun getKey(userId: UUID) = "user_logged:$userId"

    fun setLogged(user: User, logged: Boolean) {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            it.set(key, logged.toString())
        }
    }

    fun isLogged(user: User): Boolean {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            it.get(key).toBoolean()
        }
    }

    fun delete() {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            it.del("user_logged:*")
        }
    }

}