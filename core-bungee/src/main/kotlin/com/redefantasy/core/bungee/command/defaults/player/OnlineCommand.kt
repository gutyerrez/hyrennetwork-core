package com.redefantasy.core.bungee.command.defaults.player

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * @author Gutyerrez
 */
class OnlineCommand : CustomCommand("online") {

    override fun onCommand(
            commandSender: CommandSender,
            user: User?,
            args: Array<out String>
    ): Boolean {
        val users = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsers()

        val message = ComponentBuilder()
                .append("\n")
                .append("§b§l * §e${users.size} jogadores online na rede.")
                .append("\n")

        if (commandSender is ProxiedPlayer) {
            val bukkitApplication = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchBukkitApplication(user!!)
            val server = bukkitApplication?.server

            if (server !== null) {
                val usersByServer = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsersByServer(server)

                message.append("§b§l * §e${usersByServer.size} jogadores no servidor atual.")
                        .append("\n")
            }
        }

        commandSender.sendMessage(*message.create())
        return true
    }

}