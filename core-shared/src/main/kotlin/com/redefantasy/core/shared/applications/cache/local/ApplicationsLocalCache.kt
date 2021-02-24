package com.redefantasy.core.shared.applications.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.servers.data.Server
import java.net.InetSocketAddress
import java.util.stream.Collectors

/**
 * @author SrGutyerrez
 **/
class ApplicationsLocalCache : LocalCache {

    private val CACHE_BY_NAME = Caffeine
        .newBuilder()
        .build<String, Application>()

    private val CACHE_BY_ADDRESS = Caffeine
        .newBuilder()
        .build<ApplicationByAddressLookup, Application>()

    fun fetchByName(name: String) = this.CACHE_BY_NAME.getIfPresent(name)

    fun fetchByAddress(
        address: InetSocketAddress
    ) = this.CACHE_BY_ADDRESS.getIfPresent(
        ApplicationByAddressLookup(address)
    )

    fun fetchByApplicationType(applicationType: ApplicationType) = this.CACHE_BY_NAME.asMap()
        .values
        .stream()
        .filter { it.applicationType === applicationType }
        .collect(Collectors.toList())

    fun fetchByServerAndApplicationType(
        server: Server,
        applicationType: ApplicationType
    ): Application? {
        return this.CACHE_BY_NAME.asMap()
            .values
            .stream()
            .filter { it.server === server && it.applicationType === applicationType }
            .findFirst()
            .orElse(null)
    }

    override fun populate() {
        CoreProvider.Repositories.Postgres.APPLICATIONS_REPOSITORY.provide().fetchAll().forEach { name, application ->
            this.CACHE_BY_NAME.put(name, application)

            CACHE_BY_ADDRESS.put(
                ApplicationByAddressLookup(application.address),
                application
            )
        }
    }

    private data class ApplicationByAddressLookup(val address: InetSocketAddress)

}