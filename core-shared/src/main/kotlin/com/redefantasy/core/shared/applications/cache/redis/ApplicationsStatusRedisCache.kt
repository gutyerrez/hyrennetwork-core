package com.redefantasy.core.shared.applications.cache.redis

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.cache.redis.RedisCache
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
        com.redefantasy.core.shared.CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val jackson = com.redefantasy.core.shared.CoreConstants.JACKSON.writeValueAsString(applicationStatus)

            val key = this.getKey(applicationStatus.applicationName)

            it.set(key, jackson)
            it.expire(key, this.TTL_SECONDS)
        }
    }

    fun fetchApplicationStatusByApplication(application: Application, applicationStatusClass: KClass<ApplicationStatus>): ApplicationStatus? {
        return this.fetchApplicationStatusByApplicationName(application.name, applicationStatusClass)
    }

    fun fetchApplicationStatusByApplicationName(applicationName: String, applicationStatusClass: KClass<ApplicationStatus>): ApplicationStatus? {
        var applicationStatus = this.CACHE.getIfPresent(applicationName)

        if (applicationStatus != null) return applicationStatus

        val key = this.getKey(applicationName)

        com.redefantasy.core.shared.CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val value = it.get(key)

            if (value != null) {
                applicationStatus = com.redefantasy.core.shared.CoreConstants.JACKSON.readValue(value, applicationStatusClass.java)

                this.CACHE.put(applicationName, applicationStatus!!)
            }

            return applicationStatus
        }
    }

    fun fetchAllApplicationStatus(applicationStatusClass: KClass<ApplicationStatus>): Map<String, ApplicationStatus> {
        val applications = mutableMapOf<String, ApplicationStatus>()

        com.redefantasy.core.shared.CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            var cursor = ScanParams.SCAN_POINTER_START
            val scanParams = ScanParams().match("applications:*")

            do {
                val result = it.scan(cursor, scanParams)

                result.result.forEach { key ->
                    val value = it.get(key)

                    applications[key] = com.redefantasy.core.shared.CoreConstants.JACKSON.readValue(
                            value,
                            applicationStatusClass.java
                    )
                }

                cursor = result.cursor
            } while (cursor != ScanParams.SCAN_POINTER_START)
        }

        return applications
    }

    fun fetchAllApplicationStatusByApplicationsNames(applicationsNames: Collection<String>, statusClass: KClass<ApplicationStatus>): Map<String, ApplicationStatus> {
        com.redefantasy.core.shared.CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
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

                applications[applicationName] = com.redefantasy.core.shared.CoreConstants.JACKSON.readValue(
                        value,
                        statusClass.java
                )
            }

            return applications
        }
    }

    fun getResponse(applicationName: String, pipeline: Pipeline) = pipeline.get(if (applicationName.startsWith("applications:")) applicationName else this.getKey(applicationName))

}