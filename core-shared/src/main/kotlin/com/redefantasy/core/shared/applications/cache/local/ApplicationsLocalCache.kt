package com.redefantasy.core.shared.applications.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.cache.local.LocalCache
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
        .build<ApplicationAddressAndPortLookup, Application>()

    fun fetchByName(name: String) = this.CACHE_BY_NAME.getIfPresent(name)

    fun fetchByAddress(
        address: InetSocketAddress
    ) = this.CACHE_BY_ADDRESS.getIfPresent(
        ApplicationAddressAndPortLookup(address)
    )

    fun fetchByApplicationType(applicationType: ApplicationType) = this.CACHE_BY_NAME.asMap()
        .values
        .stream()
        .filter { it.applicationType === applicationType }
        .collect(Collectors.toList())

    override fun populate() {
        super.populate()

        this.CACHE_BY_NAME.asMap().values.forEach {
            CACHE_BY_ADDRESS.put(
                ApplicationAddressAndPortLookup(it.address),
                it
            )
        }
    }

    private data class ApplicationAddressAndPortLookup(val address: InetSocketAddress)

}