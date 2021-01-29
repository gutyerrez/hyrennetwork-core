package com.redefantasy.core.shared

import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.applications.status.task.ApplicationStatusTask
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
        val application = CoreProvider.prepare(10007)

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