package net.hyren.core.shared.applications.storage.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.storage.dao.ApplicationDAO
import net.hyren.core.shared.applications.storage.dto.*
import net.hyren.core.shared.applications.storage.repositories.IApplicationsRepository
import net.hyren.core.shared.applications.storage.table.ApplicationsTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgreSQLApplicationsRepository : IApplicationsRepository {

    override fun fetchAll() = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        val applications = mutableMapOf<String, Application>()

        ApplicationDAO.all().forEach {
            applications[it.name.value] = it.toApplication()
        }

        applications
    }

    override fun fetchByServer(
        fetchApplicationsByServerDTO: FetchApplicationsByServerDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        ApplicationDAO.find {
            ApplicationsTable.serverName eq fetchApplicationsByServerDTO.server.name
        }.map { it.toApplication() }
    }

    override fun fetchByType(
        fetchApplicationsByTypeDTO: FetchApplicationsByTypeDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        ApplicationDAO.find {
            ApplicationsTable.applicationType eq fetchApplicationsByTypeDTO.applicationType
        }.map { it.toApplication() }
    }

    override fun fetchByServerAndApplicationType(
        fetchApplicationsByServerAndApplicationTypeDTO: FetchApplicationsByServerAndApplicationTypeDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        ApplicationDAO.find {
            ApplicationsTable.applicationType eq fetchApplicationsByServerAndApplicationTypeDTO.applicationType and (
                ApplicationsTable.serverName eq fetchApplicationsByServerAndApplicationTypeDTO.server.name
            )
        }.firstOrNull()?.toApplication()
    }

    override fun fetchByName(
        fetchApplicationByNameDTO: FetchApplicationByNameDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        ApplicationDAO.find {
            ApplicationsTable.id eq fetchApplicationByNameDTO.applicationName
        }.firstOrNull()?.toApplication()
    }

    override fun fetchByInetSocketAddress(
        fetchApplicationByInetSocketAddressDTO: FetchApplicationByInetSocketAddressDTO
    ) = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        ApplicationDAO.find {
            ApplicationsTable.address eq fetchApplicationByInetSocketAddressDTO.inetSocketAddress.address.hostAddress and (
                ApplicationsTable.port eq fetchApplicationByInetSocketAddressDTO.inetSocketAddress.port
            )
        }.firstOrNull()?.toApplication()
    }

}