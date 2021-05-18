package net.hyren.core.shared.applications.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.storage.dto.*
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.servers.data.Server
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class ApplicationsLocalCache : LocalCache {

    private val CACHE_BY_NAME = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<String, Application> {
            CoreProvider.Repositories.PostgreSQL.APPLICATIONS_REPOSITORY.provide().fetchByName(
                FetchApplicationByNameDTO(
                    it
                )
            )
        }

    private val CACHE_BY_ADDRESS = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<InetSocketAddress, Application> {
            CoreProvider.Repositories.PostgreSQL.APPLICATIONS_REPOSITORY.provide().fetchByInetSocketAddress(
                FetchApplicationByInetSocketAddressDTO(
                    it
                )
            )
        }

    private val CACHE_BY_SERVER = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<Server, List<Application>> {
            CoreProvider.Repositories.PostgreSQL.APPLICATIONS_REPOSITORY.provide().fetchByServer(
                FetchApplicationsByServerDTO(
                    it
                )
            )
        }

    private val CACHE_BY_TYPE = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<ApplicationType, List<Application>> {
            CoreProvider.Repositories.PostgreSQL.APPLICATIONS_REPOSITORY.provide().fetchByType(
                FetchApplicationsByTypeDTO(
                    it
                )
            )
        }

    private val CACHE_BY_SERVER_AND_APPLICATION = Caffeine.newBuilder()
        .expireAfterWrite(15, TimeUnit.SECONDS)
        .build<ApplicationByServerAndApplicationTypeLookup, Application> {
            CoreProvider.Repositories.PostgreSQL.APPLICATIONS_REPOSITORY.provide().fetchByServerAndApplicationType(
                FetchApplicationsByServerAndApplicationTypeDTO(
                    it.server,
                    it.applicationType
                )
            )
        }

    fun fetchByName(name: String) = this.CACHE_BY_NAME.get(name)

    fun fetchByAddress(
        address: InetSocketAddress
    ) = this.CACHE_BY_ADDRESS.get(address)

    fun fetchByApplicationType(
        applicationType: ApplicationType
    ) = this.CACHE_BY_TYPE.get(applicationType) ?: emptyList()

    fun fetchByServerAndApplicationType(
        server: Server,
        applicationType: ApplicationType
    ) = this.CACHE_BY_SERVER_AND_APPLICATION.get(
        ApplicationByServerAndApplicationTypeLookup(
            server,
            applicationType
        )
    )

    fun fetchByServer(
        server: Server
    ) = this.CACHE_BY_SERVER.get(server) ?: emptyList()

    private class ApplicationByServerAndApplicationTypeLookup(
        val server: Server,
        val applicationType: ApplicationType
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true

            if (javaClass != other?.javaClass) return false

            other as ApplicationByServerAndApplicationTypeLookup

            if (server != other.server) return false

            if (applicationType != other.applicationType) return false

            return true
        }

        override fun hashCode(): Int {
            return server.hashCode() + applicationType.hashCode()
        }

    }

}