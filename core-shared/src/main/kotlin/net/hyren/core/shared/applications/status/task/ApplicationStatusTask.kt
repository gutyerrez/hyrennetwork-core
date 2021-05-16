package net.hyren.core.shared.applications.status.task

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.misc.json.KJson

/**
 * @author SrGutyerrez
 **/
abstract class ApplicationStatusTask(
    private val applicationStatus: ApplicationStatus
) : Runnable {

    abstract fun buildApplicationStatus(applicationStatus: ApplicationStatus)

    override fun run() {
        try {
            println("asd")

            println(KJson.encodeToString(applicationStatus))

            this.buildApplicationStatus(this.applicationStatus)

            CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().update(this.applicationStatus)

            CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchAllApplicationStatus(
                ApplicationStatus::class
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}