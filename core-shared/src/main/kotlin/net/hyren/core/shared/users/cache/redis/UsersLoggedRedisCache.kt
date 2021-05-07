package net.hyren.core.shared.users.cache.redis

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.redis.RedisCache
import net.hyren.core.shared.users.data.User
import java.util.*

/**
 * @author Gutyerrez
 */
class UsersLoggedRedisCache : RedisCache {

    fun getKey(userId: UUID) = "logged_users:$userId"

    fun setLogged(user: User, logged: Boolean) {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            if (logged) {
                it.set(key, logged.toString())
            } else it.del(key)
        }
    }

    fun isLogged(user: User): Boolean {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            if (!it.exists(key)) return@use false

            it.get(key).toBoolean()
        }
    }

    fun delete(usersId: List<UUID>) {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            usersId.forEach { userId ->
                it.del("user_logged:$userId")
            }
        }
    }

    fun delete(userId: UUID) {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            it.del("user_logged:$userId")
        }
    }

    fun delete() {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            it.del("user_logged:*")
        }
    }

}