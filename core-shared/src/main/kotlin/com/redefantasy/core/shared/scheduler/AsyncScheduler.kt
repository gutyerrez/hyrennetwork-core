package com.redefantasy.core.shared.scheduler

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
object AsyncScheduler {

    private val EXECUTOR: ScheduledExecutorService = Executors.newScheduledThreadPool(4)

    fun scheduleAsyncRepeatingTask(
        runnable: Runnable,
        initialTime: Long,
        repeatingTime: Long,
        timeUnit: TimeUnit
    ) = this.EXECUTOR.scheduleAtFixedRate(
        runnable,
        initialTime,
        repeatingTime,
        timeUnit
    )

}