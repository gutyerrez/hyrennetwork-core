package com.redefantasy.core.shared.misc.maintenance.repositories

import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author Gutyerrez
 */
interface IMaintenanceRepository : IRepository {

    fun fetchByApplication(
        application: Application
    ): Boolean

    fun update(
        application: Application,
        newState: Boolean
    )

}