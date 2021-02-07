package com.redefantasy.core.shared.users.cache.redis

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.cache.redis.RedisCache
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.users.data.User
import org.joda.time.DateTime
import redis.clients.jedis.ScanParams
import java.net.InetSocketAddress
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UsersStatusRedisCache : RedisCache {

    private val TTL_SECONDS = 10

    private fun getKey(userId: UUID) = String.format("users:$userId")

    fun isOnline(user: User): Boolean {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            return@use it.exists(key)
        }
    }

    fun fetchConnectedAddress(user: User): String? {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            return@use it.hget(key, "connected_address")
        }
    }

    fun fetchProxyApplication(user: User): Application? {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            return@use com.redefantasy.core.shared.CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(
                    it.hget(key, "proxy_application")
            )
        }
    }

    fun fetchBukkitApplication(user: User): Application? {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            return@use com.redefantasy.core.shared.CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(
                    it.hget(key, "bukkit_application")
            )
        }
    }

    fun fetchUsersByServer(server: Server): List<UUID> {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val users = mutableListOf<UUID>()
            val scanParams = ScanParams().match("users:*")

            val scan = it.scan(ScanParams.SCAN_POINTER_START, scanParams)

            scan.result.forEach { key ->
                val bukkitApplication = it.hget(key, "bukkit_application")

                val application = com.redefantasy.core.shared.CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(
                        bukkitApplication
                )

                if (application != null && application.server !== null && application.server === server) {
                    val uuid = UUID.fromString(key.split("users:")[1])

                    users.add(uuid)
                }
            }

            return@use users
        }
    }

    fun create(user: User, application: Application, address: InetSocketAddress, version: Int) {
        val map = mutableMapOf<String, String>()

        map["proxy_application"] = application.name
        map["bukkit_application"] = "undefined"
        map["connected_address"] = address.address.hostAddress
        map["connected_version"] = version.toString()
        map["joined_at"] = DateTime.now().toString()

        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val pipeline = it.pipelined()
            val key = this.getKey(user.getUniqueId())

            pipeline.hmset(key, map)
            pipeline.expire(key, this.TTL_SECONDS)
            pipeline.sync()
        }
    }

    fun delete(user: User) {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            it.del(key)
        }
    }

}