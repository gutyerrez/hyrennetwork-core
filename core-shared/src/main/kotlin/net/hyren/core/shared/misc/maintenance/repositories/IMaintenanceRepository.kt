package net.hyren.core.shared.misc.maintenance.repositories

import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.storage.repositories.IRepository

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