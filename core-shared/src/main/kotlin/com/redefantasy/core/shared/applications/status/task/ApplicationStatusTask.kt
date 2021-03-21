package com.redefantasy.core.shared.applications.status.task

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
abstract class ApplicationStatusTask(
        private val applicationStatus: ApplicationStatus
) : Runnable {

    private var lastPoint = 0L

    abstract fun buildApplicationStatus(applicationStatus: ApplicationStatus)

    override fun run() {
        this.buildApplicationStatus(this.applicationStatus)

        CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().update(this.applicationStatus)
        CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchAllApplicationStatus(
            ApplicationStatus::class
        )

        val now = System.currentTimeMillis()

        if (now - lastPoint >= TimeUnit.SECONDS.toMillis(10)) {
            lastPoint = now

            CoreProvider.Databases.Influx.INFLUX_MAIN.provide().write(
                    this.applicationStatus.buildPoint()
                            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                            .build()
            )
        }
    }

}