package com.redefantasy.core.shared.applications.storage.repositories

import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.storage.dto.FetchApplicationByAddressAndPortDTO
import com.redefantasy.core.shared.applications.storage.dto.FetchApplicationByNameDTO
import com.redefantasy.core.shared.applications.storage.dto.FetchApplicationsByTypeAndServerDTO
import com.redefantasy.core.shared.applications.storage.dto.FetchApplicationsByTypeDTO
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IApplicationsRepository : IRepository {

    fun fetchAll(): Map<String, Application>

    fun fetchByType(fetchApplicationsByTypeDTO: FetchApplicationsByTypeDTO): Map<String, Application>

    fun fetchByTypeAndServer(fetchApplicationsByTypeAndServerDTO: FetchApplicationsByTypeAndServerDTO): Map<String, Application>

    fun fetchByName(fetchApplicationByNameDTO: FetchApplicationByNameDTO): Application?

    fun fetchByAddressAndPort(fetchApplicationByAddressAndPortDTO: FetchApplicationByAddressAndPortDTO): Application?

}