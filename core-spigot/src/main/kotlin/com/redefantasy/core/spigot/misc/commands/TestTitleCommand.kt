package com.redefantasy.core.spigot.misc.commands

import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.spigot.command.CustomCommand
import com.redefantasy.core.spigot.misc.utils.Title
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class TestTitleCommand : CustomCommand("test") {

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean {
        val title = args.joinToString(" ").split("\\n")[0]
        val subTitle = args.joinToString(" ").split("\\n")[1]

        val _title = Title(title, subTitle, 0, 30, 0)

        commandSender as Player

        _title.sendToPlayer(commandSender)
        return true
    }

}