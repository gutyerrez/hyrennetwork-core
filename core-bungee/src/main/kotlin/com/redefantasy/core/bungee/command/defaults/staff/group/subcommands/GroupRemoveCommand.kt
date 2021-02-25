package com.redefantasy.core.bungee.command.defaults.staff.group.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.staff.group.GroupCommand
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender

/**
 * @author Gutyerrez
 */
class GroupRemoveCommand : CustomCommand("remover") {

    override fun getDescription() = "Remover um grupo de um usuário."

    override fun getArguments() = listOf(
        Argument("usuário"),
        Argument("grupo"),
        Argument("servidor")
    )

    override fun getParent() = GroupCommand()

    override fun onCommand(
            commandSender: CommandSender,
            user: User?,
            args: Array<out String>
    ): Boolean {
        return false
    }

}