package com.redefantasy.core.shared.applications.cache.redis

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.cache.redis.RedisCache
import com.redefantasy.core.shared.servers.data.Server
import redis.clients.jedis.Pipeline
import redis.clients.jedis.Response
import redis.clients.jedis.ScanParams
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * @author SrGutyerrez
 **/
class ApplicationsStatusRedisCache : RedisCache {

    private val TTL_SECONDS = 5

    private val CACHE = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build<String, ApplicationStatus>()

    private fun getKey(name: String) = "applications:$name"

    fun update(applicationStatus: ApplicationStatus) {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val pipeline = it.pipelined()
            val key = this.getKey(applicationStatus.applicationName)

            val json = CoreConstants.JACKSON.writeValueAsString(
                applicationStatus
            )

            pipeline.set(key, json)
            pipeline.expire(key, this.TTL_SECONDS)
            pipeline.sync()
        }
    }

    fun fetchApplicationStatusByApplication(
        application: Application,
        applicationStatusClass: KClass<out ApplicationStatus>
    ): ApplicationStatus? {
        return this.fetchApplicationStatusByApplicationName(
            application.name,
            applicationStatusClass
        )
    }

    fun fetchApplicationStatusByServer(
        server: Server,
        applicationStatusClass: KClass<out ApplicationStatus>
    ): Map<String, ApplicationStatus?> {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val applicationStatuses = mutableMapOf<String, ApplicationStatus?>()
            val scanParams = ScanParams().match("applications:*")

            do {
                var cursor = ScanParams.SCAN_POINTER_START
                val result = it.scan(cursor, scanParams)

                result.result.forEach { key ->
                    val value = it.get(key)

                    val applicationStatus = CoreConstants.JACKSON.readValue(
                        value,
                        applicationStatusClass.java
                    )

                    if (applicationStatus.server == server)
                        applicationStatuses[key] = applicationStatus
                }

                cursor = result.cursor
            } while (cursor != ScanParams.SCAN_POINTER_START)

            return@use applicationStatuses
        }
    }

    fun fetchApplicationStatusByApplicationName(
        applicationName: String,
        applicationStatusClass: KClass<out ApplicationStatus>
    ): ApplicationStatus? {
        var applicationStatus = this.CACHE.getIfPresent(applicationName)

        if (applicationStatus != null) return applicationStatus

        val key = this.getKey(applicationName)

        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val value = it.get(key)

            if (value != null) {
                applicationStatus = CoreConstants.JACKSON.readValue(
                    value,
                    applicationStatusClass.java
                )

                this.CACHE.put(applicationName, applicationStatus!!)
            }

            return applicationStatus
        }
    }

    fun fetchAllApplicationStatus(
        applicationStatusClass: KClass<out ApplicationStatus>
    ): Map<String, ApplicationStatus> {
        val applicationsStatuses = mutableMapOf<String, ApplicationStatus>()

        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            var cursor = ScanParams.SCAN_POINTER_START
            val scanParams = ScanParams().match("applications:*")

            do {
                val result = it.scan(cursor, scanParams)

                result.result.forEach { key ->
                    val value = it.get(key)

                    applicationsStatuses[key] = CoreConstants.JACKSON.readValue(
                            value,
                            applicationStatusClass.java
                    )
                }

                cursor = result.cursor
            } while (cursor != ScanParams.SCAN_POINTER_START)
        }

        return applicationsStatuses
    }

    fun fetchAllApplicationStatusByApplicationsNames(
        applicationsNames: Collection<String>,
        statusClass: KClass<out ApplicationStatus>
    ): Map<String, ApplicationStatus> {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val pipeline = it.pipelined()

            val applications = mutableMapOf<String, ApplicationStatus>()

            val responses = mutableMapOf<String, Response<String>>()

            applicationsNames.forEach { name ->
                val status = this.CACHE.getIfPresent(name)

                if (status != null) {
                    applications[name] = status
                } else {
                    val response = this.getResponse(name, pipeline)

                    responses[name] = response
                }
            }

            pipeline.sync()

            responses.forEach { entry ->
                val applicationName = entry.key
                val response = entry.value

                val value = response.get()

                applications[applicationName] = CoreConstants.JACKSON.readValue(
                        value,
                        statusClass.java
                )
            }

            return applications
        }
    }

    fun getResponse(
        applicationName: String,
        pipeline: Pipeline
    ) = pipeline.get(if (applicationName.startsWith("applications:")) applicationName else this.getKey(applicationName))

}