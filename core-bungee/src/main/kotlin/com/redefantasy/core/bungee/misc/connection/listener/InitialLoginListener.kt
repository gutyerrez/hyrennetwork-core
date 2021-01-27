package com.redefantasy.core.bungee.misc.connection.listener

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.event.InitialLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

/**
 * @author Gutyerrez
 */
class InitialLoginListener : Listener {

    @EventHandler
    fun on(event: InitialLoginEvent) {
        val applications = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(ApplicationType.LOBBY)

        val liveApplication = applications.stream().sorted { application1, application2 ->
            val applicationStatus1 = CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
                application1,
                ApplicationStatus::class
            )
            val applicationStatus2 = CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
                application2,
                ApplicationStatus::class
            )

            if (applicationStatus1 === null || applicationStatus2 === null) return@sorted 0

            applicationStatus2.onlinePlayers.compareTo(applicationStatus1.onlinePlayers)
        }.findFirst().orElse(null)

        if (liveApplication === null) return

        event.targetServerInfo = ServerInfo { liveApplication.address }
    }

}