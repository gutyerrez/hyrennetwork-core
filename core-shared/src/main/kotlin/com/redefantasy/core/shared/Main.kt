package com.redefantasy.core.shared

import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.applications.status.task.ApplicationStatusTask
import java.net.InetSocketAddress
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
object Main {

    private val STATUS_EXECUTOR = Executors.newScheduledThreadPool(4)

    @JvmStatic
    fun main(args: Array<String>) {
        val application = Application(
                "core-application",
                "Core Application",
                address = InetSocketAddress(
                        "127.0.0.1",
                        0
                ),
                applicationType = ApplicationType.GENERIC
        )

        com.redefantasy.core.shared.CoreProvider.prepare(application)

        this.STATUS_EXECUTOR.scheduleAtFixedRate(object : ApplicationStatusTask(
                ApplicationStatus(
                        application.name,
                        application.applicationType,
                        application.server,
                        application.address,
                        System.currentTimeMillis()
                )
        ) {
            override fun buildApplicationStatus(applicationStatus: ApplicationStatus) {
                val runtime = Runtime.getRuntime()

                applicationStatus.heapSize = runtime.totalMemory()
                applicationStatus.heapMaxSize = runtime.maxMemory()
                applicationStatus.heapFreeSize = runtime.freeMemory()
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

}