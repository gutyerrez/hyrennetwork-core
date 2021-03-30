package com.redefantasy.core.shared.applications.storage.repositories.implementations

import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.storage.dao.ApplicationDAO
import com.redefantasy.core.shared.applications.storage.dto.*
import com.redefantasy.core.shared.applications.storage.repositories.IApplicationsRepository
import com.redefantasy.core.shared.applications.storage.table.ApplicationsTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresApplicationsRepository : IApplicationsRepository {

    override fun fetchAll(): Map<String, Application> {
        return transaction {
            val applications = mutableMapOf<String, Application>()

            ApplicationDAO.all().forEach {
                applications[it.name.value] = it.asApplication()
            }

            return@transaction applications
        }
    }

    override fun fetchByServer(
        fetchApplicationsByServerDTO: FetchApplicationsByServerDTO
    ): List<Application> {
        return transaction {
            return@transaction ApplicationDAO.find {
                ApplicationsTable.serverName eq fetchApplicationsByServerDTO.server.name
            }.map { it.asApplication() }
        }
    }

    override fun fetchByType(
        fetchApplicationsByTypeDTO: FetchApplicationsByTypeDTO
    ): List<Application> {
        return transaction {
            return@transaction ApplicationDAO.find {
                ApplicationsTable.applicationType eq fetchApplicationsByTypeDTO.applicationType
            }.map { it.asApplication() }
        }
    }

    override fun fetchByServerAndApplicationType(
        fetchApplicationsByServerAndApplicationTypeDTO: FetchApplicationsByServerAndApplicationTypeDTO
    ): Application? {
        return transaction {
            val result = ApplicationDAO.find {
                ApplicationsTable.applicationType eq fetchApplicationsByServerAndApplicationTypeDTO.applicationType and (
                        ApplicationsTable.serverName eq fetchApplicationsByServerAndApplicationTypeDTO.server.name
                )
            }

            if (result.empty()) return@transaction null

            return@transaction result.first().asApplication()
        }
    }

    override fun fetchByName(
        fetchApplicationByNameDTO: FetchApplicationByNameDTO
    ): Application? {
        return transaction {
            var application: Application? = null

            val result = ApplicationDAO.find {
                ApplicationsTable.id eq fetchApplicationByNameDTO.applicationName
            }

            if (!result.empty()) application = result.first().asApplication()

            return@transaction application
        }
    }

    override fun fetchByInetSocketAddress(
        fetchApplicationByInetSocketAddressDTO: FetchApplicationByInetSocketAddressDTO
    ): Application? {
        return transaction {
            var application: Application? = null

            val result = ApplicationDAO.find {
                ApplicationsTable.address eq fetchApplicationByInetSocketAddressDTO.inetSocketAddress.address.hostAddress and (
                        ApplicationsTable.port eq fetchApplicationByInetSocketAddressDTO.inetSocketAddress.port
                )
            }

            if (!result.empty()) application = result.first().asApplication()

            return@transaction application
        }
    }

}