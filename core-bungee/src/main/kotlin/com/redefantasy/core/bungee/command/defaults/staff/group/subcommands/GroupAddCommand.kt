package com.redefantasy.core.bungee.command.defaults.staff.group.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.staff.group.GroupCommand
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender

/**
 * @author Gutyerrez
 */
class GroupAddCommand : CustomCommand("adicionar") {

    override fun getDescription() = "Adicionar um grupo a um usuário."

    override fun getArguments() = listOf(
        Argument("usuário"),
        Argument("grupo"),
        Argument("servidor"),
        Argument("duração")
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