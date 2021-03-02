package com.redefantasy.core.bungee.command.defaults.player

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.status.ApplicationStatus
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * @author Gutyerrez
 */
class LobbyCommand : CustomCommand("lobby") {

    override fun getDescription() = "Teleportar-se para um saguão."

    override fun onCommand(
            commandSender: CommandSender,
            user: User?,
            args: Array<out String>
    ): Boolean {
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

            if (applicationStatus1.onlinePlayers >= application1.slots ?: 0 || applicationStatus2.onlinePlayers >= application2.slots ?: 0) return@sorted 0

            applicationStatus2.onlinePlayers.compareTo(applicationStatus1.onlinePlayers)
        }.findFirst().orElse(null)

        if (liveApplication === null) {
            commandSender.sendMessage(TextComponent("§cNão foi possível localizar um saguão disponível."))
            return false
        }

        commandSender as ProxiedPlayer

        commandSender.sendMessage(TextComponent("§aConectando..."))
        commandSender.connect { liveApplication.address }
        return true
    }

}