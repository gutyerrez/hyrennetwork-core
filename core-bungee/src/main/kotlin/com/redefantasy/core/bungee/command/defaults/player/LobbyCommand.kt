package com.redefantasy.core.bungee.command.defaults.player

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender

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
        return false
    }

}