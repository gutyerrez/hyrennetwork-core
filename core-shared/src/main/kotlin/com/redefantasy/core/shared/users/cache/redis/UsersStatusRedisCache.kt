package com.redefantasy.core.shared.users.cache.redis

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.cache.redis.RedisCache
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.users.data.User
import org.joda.time.DateTime
import redis.clients.jedis.ScanParams
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UsersStatusRedisCache : RedisCache {

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

            if (!it.hexists(key, "connected_address")) return null

            return@use it.hget(key, "connected_address")
        }
    }

    fun fetchProxyApplication(user: User): Application? {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            if (!it.hexists(key, "proxy_application")) return null

            return@use CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(
                it.hget(key, "proxy_application")
            )
        }
    }

    fun fetchBukkitApplication(user: User): Application? {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            if (!it.hexists(key, "bukkit_application")) return null

            return@use CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(
                it.hget(key, "bukkit_application")
            )
        }
    }

    fun fetchUsers(): List<UUID> {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val users = mutableListOf<UUID>()

            try {
                val scanParams = ScanParams()

                scanParams.match("users:*")

                var cursor = "0"

                do {
                    val scan = it.scan(cursor, scanParams)

                    scan.result.forEach { key ->
                        val uuid = UUID.fromString(key.split("users:")[1])

                        users.add(uuid)
                    }

                    cursor = scan.cursor
                } while (cursor != "0")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return@use users
        }
    }

    fun fetchUsersByProxyApplication(application: Application): List<UUID> {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val users = mutableListOf<UUID>()

            val scanParams = ScanParams()

            scanParams.match("users:*")

            var cursor = "0"

            do {
                val scan = it.scan(cursor, scanParams)

                scan.result.forEach { key ->
                    val proxyApplicationName = it.hget(key, "proxy_application")

                    val proxyApplication =
                        CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(proxyApplicationName)

                    if (proxyApplication === application) {
                        val uuid = UUID.fromString(key.split("users:")[1])

                        users.add(uuid)
                    }
                }

                cursor = scan.cursor
            } while (cursor != "0")

            return@use users
        }
    }

    fun fetchUsersByServer(server: Server): List<UUID> {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val users = mutableListOf<UUID>()
            val scanParams = ScanParams()

            scanParams.match("users:*")

            var cursor = "0"

            do {
                val scan = it.scan(cursor, scanParams)

                scan.result.forEach { key ->
                    val bukkitApplication = it.hget(key, "bukkit_application")

                    val application = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(
                        bukkitApplication
                    )

                    if (application != null && application.server !== null && application.server === server) {
                        val uuid = UUID.fromString(key.split("users:")[1])

                        users.add(uuid)
                    }
                }

                cursor = scan.cursor
            } while (cursor != "0")

            return@use users
        }
    }

    fun fetchJoinedAt(user: User): DateTime? {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            if (!it.hexists(key, "joined_at")) return@use null

            return@use DateTime.parse(it.hget(key, "joined_at"))
        }
    }

    fun create(user: User, application: Application?, version: Int) {
        try {
            println("Criar status do usuário ${user.id}")

            val map = mutableMapOf<String, String>()

            map["proxy_application"] = CoreProvider.application.name
            map["bukkit_application"] = application?.name ?: "desconhecida"
            map["connected_address"] = CoreProvider.application.address.address.hostAddress
            map["connected_version"] = version.toString()
            map["joined_at"] = if (this.fetchJoinedAt(user) === null) {
                DateTime.now().toString()
            } else this.fetchJoinedAt(user).toString()

            CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
                try {
                    val pipeline = it.pipelined()
                    val key = this.getKey(user.getUniqueId())

                    pipeline.hmset(key, map)
                    pipeline.expire(key, 10)
                    pipeline.sync()

                    println("Criei")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(application: Application) {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val scanParams = ScanParams().match("users:*")

            val scan = it.scan(ScanParams.SCAN_POINTER_START, scanParams)

            scan.result.forEach { key ->
                val proxyApplicationName = it.hget(key, "proxy_application")

                val proxyApplication = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(proxyApplicationName)

                if (proxyApplication === application) it.del(key)
            }
        }
    }

    fun delete() {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            it.del("users:*")
        }
    }

    fun delete(user: User) {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val key = this.getKey(user.getUniqueId())

            it.del(key)
        }
    }

}