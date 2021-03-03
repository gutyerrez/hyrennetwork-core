package com.redefantasy.core.bungee.command.defaults.staff.group.subcommands

import com.google.common.base.Enums
import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.staff.group.GroupCommand
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.groups.due.storage.dto.DeleteUserGroupDueDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

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
        val targetUser = CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])
        val group = Enums.getIfPresent(Group::class.java, args[1])
        val server = CoreProvider.Cache.Local.SERVERS.provide().fetchByName(args[2])

        if (targetUser === null) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
            return false
        }

        if (group === null || !group.isPresent) {
            commandSender.sendMessage(TextComponent("§cEste grupo não existe"))
            return false
        }

        if (!targetUser.hasStrictGroup(group.get(), server)) {
            commandSender.sendMessage(TextComponent("§cEste usuário não possui esse grupo."))
            return false
        }

        if ((user!!.getHighestGroup().priority == group.get().priority) && !CoreConstants.WHITELISTED_USERS.contains(user.name)) {
            commandSender.sendMessage(TextComponent("§cVocê não pode gerenciar este grupo."))
            return false
        }

        if (CoreProvider.Repositories.Postgres.USERS_GROUPS_DUE_REPOSITORY.provide().delete(
                DeleteUserGroupDueDTO(
                        targetUser.id,
                        group.get(),
                        server
                )
        )) {
            commandSender.sendMessage(TextComponent("§aVocê removeu o grupo ${group.get().displayName} do usuário ${targetUser.name}."))
            return true
        } else {
            commandSender.sendMessage(TextComponent("§cNão foi possível remover o grupo do usuário."))
            return false
        }
    }

}