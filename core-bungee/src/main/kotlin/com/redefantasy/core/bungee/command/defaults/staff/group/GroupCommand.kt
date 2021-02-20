package com.redefantasy.core.bungee.command.defaults.staff.group

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.staff.group.subcommands.GroupAddCommand
import com.redefantasy.core.bungee.command.defaults.staff.group.subcommands.GroupRemoveCommand
import com.redefantasy.core.shared.commands.restriction.entities.implementations.GroupCommandRestrictable
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender

/**
 * @author Gutyerrez
 */
class GroupCommand : CustomCommand("grupo"), GroupCommandRestrictable {

    override fun getDescription() = "Gerenciar grupos de um usuário"

    override fun getGroup() = Group.MANAGER

    override fun getSubCommands() = listOf(
        GroupAddCommand(),
        GroupRemoveCommand()
    )

    override fun onCommand(commandSender: CommandSender, user: User?, args: Array<out String>): Boolean {
        TODO("Not yet implemented")
    }

}