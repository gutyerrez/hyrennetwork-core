package com.redefantasy.core.shared.users.reports.cache.redis

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.redis.RedisCache
import com.redefantasy.core.shared.users.reports.data.Report
import redis.clients.jedis.ScanParams
import java.util.*

/**
 * @author SrGutyerrez
 **/
class UsersReportsRedisCache : RedisCache {

    fun fetchAll(): Map<UUID, List<Report>> {
        val reports = mutableMapOf<UUID, List<Report>>()

        com.redefantasy.core.shared.CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val scanParams = ScanParams().match("reports:*")
            val result = it.scan(ScanParams.SCAN_POINTER_START, scanParams)

            result.result.forEach { key ->
                val userId = UUID.fromString(key.split("reports:")[1])
                val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)

                if (user == null)
                    throw NullPointerException("Can't find user")

                if (user.isOnline()) {
                    val value = it.get(key)

                    val _reports = CoreConstants.JACKSON.readValue(
                            value,
                            Array<Report>::class.java
                    )

                    reports[userId] = _reports.asList()
                }
            }
        }

        return reports
    }

    fun fetchByUserId(userId: UUID): MutableList<Report> {
        val reports = mutableListOf<Report>()

        com.redefantasy.core.shared.CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val value = it.get("reports:${userId}")

            if (value != null) {
                val _reports = CoreConstants.JACKSON.readValue(
                        value,
                        Array<Report>::class.java
                )

                reports.addAll(_reports)
            }
        }

        return reports
    }

    fun create(userId: UUID, report: Report) {
        val reports = this.fetchByUserId(userId)

        reports.add(report)

        com.redefantasy.core.shared.CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val value = com.redefantasy.core.shared.CoreConstants.JACKSON.writeValueAsString(reports.toTypedArray())

            it.set("reports:$userId", value)
        }
    }

    fun delete(userId: UUID) {
        com.redefantasy.core.shared.CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            it.del("reports:$userId")
        }
    }

}