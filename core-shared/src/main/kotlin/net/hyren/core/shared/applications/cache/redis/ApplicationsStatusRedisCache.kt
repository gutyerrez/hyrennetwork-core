package net.hyren.core.shared.applications.cache.redis

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.cache.redis.RedisCache
import net.hyren.core.shared.misc.json.KJson
import net.hyren.core.shared.servers.data.Server
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

    fun update(
        applicationStatus: ApplicationStatus
    ) {
        println("Aopa")

        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val pipeline = it.pipelined()
            val key = this.getKey(applicationStatus.applicationName)

            println(KJson.encodeToString(applicationStatus))

            pipeline.set(key, KJson.encodeToString(applicationStatus))
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
    ): Map<String, ApplicationStatus> {
        return CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val applicationStatuses = mutableMapOf<String, ApplicationStatus>()
            val scanParams = ScanParams().match("applications:*")

            do {
                var cursor = ScanParams.SCAN_POINTER_START
                val result = it.scan(cursor, scanParams)

                result.result.forEach { key ->
                    val value = it.get(key)

                    val applicationStatus = KJson.decodeFromString<ApplicationStatus>(value)

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
        applicationStatusKClass: KClass<out ApplicationStatus>
    ): ApplicationStatus? {
        val key = this.getKey(applicationName)

        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val value = it.get(key)

            if (value != null) {
                this.CACHE.put(
                    applicationName,
                    KJson.decodeFromString(applicationStatusKClass, value) as ApplicationStatus
                )
            }
        }

        return CACHE.getIfPresent(key)
    }

    fun fetchAllApplicationStatus(
        applicationStatusKClass: KClass<out ApplicationStatus>
    ): Map<String, ApplicationStatus> {
        val applicationsStatuses = mutableMapOf<String, ApplicationStatus>()

        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            var cursor = ScanParams.SCAN_POINTER_START
            val scanParams = ScanParams().match("applications:*")

            do {
                val result = it.scan(cursor, scanParams)

                result.result.forEach { key ->
                    val value = it.get(key)

                    applicationsStatuses[key] = KJson.decodeFromString(
                        applicationStatusKClass,
                        value
                    ) as ApplicationStatus
                }

                cursor = result.cursor
            } while (cursor != ScanParams.SCAN_POINTER_START)
        }

        return applicationsStatuses
    }

    fun fetchAllApplicationStatusByApplicationsNames(
        applicationsNames: Collection<String>,
        applicationStatusKClass: KClass<out ApplicationStatus>
    ): Map<String, ApplicationStatus> {
        CoreProvider.Databases.Redis.REDIS_MAIN.provide().resource.use {
            val pipeline = it.pipelined()

            val applicationsStatuses = mutableMapOf<String, ApplicationStatus>()

            val responses = mutableMapOf<String, Response<String>>()

            applicationsNames.forEach { name ->
                val status = this.CACHE.getIfPresent(name)

                if (status != null) {
                    applicationsStatuses[name] = status
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

                applicationsStatuses[applicationName] = KJson.decodeFromString(
                    applicationStatusKClass,
                    value
                ) as ApplicationStatus
            }

            return applicationsStatuses
        }
    }

    fun getResponse(
        applicationName: String,
        pipeline: Pipeline
    ) = pipeline.get(if (applicationName.startsWith("applications:")) applicationName else this.getKey(applicationName))

}