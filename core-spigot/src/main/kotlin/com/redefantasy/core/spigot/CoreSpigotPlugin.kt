package com.redefantasy.core.spigot

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.applications.status.task.ApplicationStatusTask
import com.redefantasy.core.shared.scheduler.AsyncScheduler
import com.redefantasy.core.shared.wrapper.CoreWrapper
import com.redefantasy.core.spigot.echo.packets.listener.SoundEchoPacketListener
import com.redefantasy.core.spigot.echo.packets.listener.TitleEchoPacketListener
import com.redefantasy.core.spigot.listeners.GeneralListener
import com.redefantasy.core.spigot.misc.plugin.CustomPlugin
import com.redefantasy.core.spigot.wrapper.SpigotWrapper
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class CoreSpigotPlugin : CustomPlugin(true) {

    companion object {

        lateinit var instance: CustomPlugin

    }

    init {
        instance = this
    }

    private var onlineSince = 0L

    override fun onEnable() {
        super.onEnable()

        CoreSpigotProvider.prepare()

        this.onlineSince = System.currentTimeMillis()

        CoreWrapper.WRAPPER = SpigotWrapper()

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(TitleEchoPacketListener())
        CoreProvider.Databases.Redis.ECHO.provide().registerListener(SoundEchoPacketListener())

        val pluginManager = Bukkit.getServer().pluginManager

        pluginManager.registerEvents(GeneralListener(), this)
        pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun on(
                event: PlayerJoinEvent
            ) {
                event.joinMessage = null
            }

            @EventHandler
            fun on(
                event: PlayerQuitEvent
            ) {
                event.quitMessage = null
            }
        }, this)

        /**
         * Start server task
         */
        if (CoreProvider.application.server == CoreProvider.Cache.Local.SERVERS.provide().fetchByName("FACTIONS_OMEGA")) {
            AsyncScheduler.scheduleAsyncRepeatingTask(
                object : ApplicationStatusTask(
                    ApplicationStatus(
                        CoreProvider.application.name,
                        CoreProvider.application.applicationType,
                        CoreProvider.application.server,
                        CoreProvider.application.address,
                        this.onlineSince
                    )
                ) {
                    override fun buildApplicationStatus(
                        applicationStatus: ApplicationStatus
                    ) {
                        val runtime = Runtime.getRuntime()

                        applicationStatus.heapSize = runtime.totalMemory()
                        applicationStatus.heapMaxSize = runtime.maxMemory()
                        applicationStatus.heapFreeSize = runtime.freeMemory()
                    }
                },
                0,
                1,
                TimeUnit.SECONDS
            )
        }
    }

}