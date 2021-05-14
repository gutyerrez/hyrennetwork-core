package net.hyren.core.shared.users.reports.cache.redis

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.redis.RedisCache
import net.hyren.core.shared.users.reports.data.Report
import redis.clients.jedis.ScanParams
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UsersReportsRedisCache : RedisCache {

    fun fetchAll(): Map<UUID, List<Report>> {
        val reports = mutableMapOf<UUID, List<Report>>()

        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val scanParams = ScanParams().match("reports:*")
            val result = it.scan(ScanParams.SCAN_POINTER_START, scanParams)

            result.result.forEach { key ->
                val userId = UUID.fromString(key.split("reports:")[1])
                val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)

                if (user == null)
                    throw NullPointerException("Can't find user")

                if (user.isOnline()) {
                    val value = it.get(key)

                    val _reports = Json.decodeFromString<Array<Report>>(value)

                    reports[userId] = _reports.asList()
                }
            }
        }

        return reports
    }

    fun fetchByUserId(userId: UUID): MutableList<Report> {
        val reports = mutableListOf<Report>()

        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val value = it.get("reports:${userId}")

            if (value != null) {
                val _reports = Json.decodeFromString<Array<Report>>(value)

                reports.addAll(_reports)
            }
        }

        return reports
    }

    fun create(userId: UUID, report: Report) {
        val reports = this.fetchByUserId(userId)

        reports.add(report)

        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val value = Json.encodeToString(report)

            it.set("reports:$userId", value)
        }
    }

    fun delete(userId: UUID) {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            it.del("reports:$userId")
        }
    }

}