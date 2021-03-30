package com.redefantasy.core.shared.applications.storage.repositories

import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.storage.dto.*
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IApplicationsRepository : IRepository {

    fun fetchAll(): Map<String, Application>

    fun fetchByServer(
        fetchApplicationsByServerDTO: FetchApplicationsByServerDTO
    ): List<Application>

    fun fetchByType(
        fetchApplicationsByTypeDTO: FetchApplicationsByTypeDTO
    ): List<Application>

    fun fetchByServerAndApplicationType(
        fetchApplicationsByServerAndApplicationTypeDTO: FetchApplicationsByServerAndApplicationTypeDTO
    ): Application?

    fun fetchByName(
        fetchApplicationByNameDTO: FetchApplicationByNameDTO
    ): Application?

    fun fetchByInetSocketAddress(
        fetchApplicationByInetSocketAddressDTO: FetchApplicationByInetSocketAddressDTO
    ): Application?

}