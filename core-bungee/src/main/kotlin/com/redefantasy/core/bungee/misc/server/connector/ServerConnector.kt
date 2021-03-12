package com.redefantasy.core.bungee.misc.server.connector

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.table.UsersTable
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.connection.ServerConnector
import org.jetbrains.exposed.dao.id.EntityID
import java.net.InetSocketAddress

/**
 * @author Gutyerrez
 */
class ServerConnector : ServerConnector {

    override fun fetchLobbyServer(): InetSocketAddress? {
        val applications = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(ApplicationType.LOGIN)

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

            if (applicationStatus1.onlinePlayers < application1.slots ?: 0 && applicationStatus2.onlinePlayers < application2.slots ?: 0)
                return@sorted applicationStatus2.onlinePlayers.compareTo(applicationStatus1.onlinePlayers)

            return@sorted 0
        }.findFirst().orElse(null)

        if (liveApplication === null) return null

        return liveApplication.address
    }

    override fun changedUserApplication(
        proxiedPlayer: ProxiedPlayer,
        bukkitApplicationAddress: InetSocketAddress
    ) {
        println(bukkitApplicationAddress)

        val bukkitApplication = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByAddress(
            bukkitApplicationAddress
        )

        if (bukkitApplication === null) {
            val disconnectMessage = ComponentBuilder()
                .append("§c§lREDE FANTASY")
                .append("\n\n")
                .append("§cNão foi possível localizar a aplicação.")
                .create()

            proxiedPlayer.disconnect(*disconnectMessage)
            return
        }

        var user = CoreProvider.Cache.Local.USERS.provide().fetchById(proxiedPlayer.uniqueId)

        if (user === null) user = User(
            EntityID(proxiedPlayer.uniqueId, UsersTable),
            proxiedPlayer.name
        )

        CoreProvider.Cache.Redis.USERS_STATUS.provide().create(
            user,
            bukkitApplication,
            proxiedPlayer.pendingConnection.version
        )
    }

}