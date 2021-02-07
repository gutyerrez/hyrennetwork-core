package com.redefantasy.core.bungee.misc.server.connector

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import net.md_5.bungee.BungeeServerInfo
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ServerConnector
import java.net.InetSocketAddress

/**
 * @author Gutyerrez
 */
class ServerConnector : ServerConnector {

    override fun fetchLobbyServer(): ServerInfo? {
//        val applications = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(ApplicationType.LOBBY)
//
//        val liveApplication = applications.stream().sorted { application1, application2 ->
//            val applicationStatus1 = CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
//                application1,
//                ApplicationStatus::class
//            )
//            val applicationStatus2 = CoreProvider.Cache.Redis.APPLICATIONS_STATUS.provide().fetchApplicationStatusByApplication(
//                application2,
//                ApplicationStatus::class
//            )
//
//            if (applicationStatus1 === null || applicationStatus2 === null) return@sorted 0
//
//            applicationStatus2.onlinePlayers.compareTo(applicationStatus1.onlinePlayers)
//        }.findFirst().orElse(null)
//
//        if (liveApplication === null) return null

        return BungeeServerInfo(
            InetSocketAddress("158.69.120.87", 10003)
        )
    }

}