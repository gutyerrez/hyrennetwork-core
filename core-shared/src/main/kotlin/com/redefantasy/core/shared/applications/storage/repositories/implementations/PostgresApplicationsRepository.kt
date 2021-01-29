package com.redefantasy.core.shared.applications.storage.repositories.implementations

import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.storage.dao.ApplicationDAO
import com.redefantasy.core.shared.applications.storage.dto.FetchApplicationByAddressAndPortDTO
import com.redefantasy.core.shared.applications.storage.dto.FetchApplicationByNameDTO
import com.redefantasy.core.shared.applications.storage.dto.FetchApplicationsByTypeAndServerDTO
import com.redefantasy.core.shared.applications.storage.dto.FetchApplicationsByTypeDTO
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

    override fun fetchByType(fetchApplicationsByTypeDTO: FetchApplicationsByTypeDTO): Map<String, Application> {
        return transaction {
            val applications = mutableMapOf<String, Application>()

            ApplicationDAO.find {
                ApplicationsTable.applicationType eq fetchApplicationsByTypeDTO.applicationType
            }.forEach {
                applications[it.name.value] = it.asApplication()
            }

            return@transaction applications
        }
    }

    override fun fetchByTypeAndServer(fetchApplicationsByTypeAndServerDTO: FetchApplicationsByTypeAndServerDTO): Map<String, Application> {
        return transaction {
            val applications = mutableMapOf<String, Application>()

            ApplicationDAO.find {
                ApplicationsTable.applicationType eq fetchApplicationsByTypeAndServerDTO.applicationType and (
                        ApplicationsTable.serverName eq fetchApplicationsByTypeAndServerDTO.server.name
                )
            }.forEach {
                applications[it.name.value] = it.asApplication()
            }

            return@transaction applications
        }
    }

    override fun fetchByName(fetchApplicationByNameDTO: FetchApplicationByNameDTO): Application? {
        return transaction {
            var application: Application? = null

            val result = ApplicationDAO.find {
                ApplicationsTable.id eq fetchApplicationByNameDTO.applicationName
            }

            if (!result.empty()) application = result.first().asApplication()

            return@transaction application
        }
    }

    override fun fetchByAddressAndPort(fetchApplicationByAddressAndPortDTO: FetchApplicationByAddressAndPortDTO): Application? {
        return transaction {
            var application: Application? = null

            val result = ApplicationDAO.find {
                ApplicationsTable.address eq fetchApplicationByAddressAndPortDTO.address and (
                        ApplicationsTable.port eq fetchApplicationByAddressAndPortDTO.port
                )
            }

            if (!result.empty()) application = result.first().asApplication()

            return@transaction application
        }
    }

}