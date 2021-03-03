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
import com.redefantasy.core.shared.users.groups.due.storage.dto.CreateUserGroupDueDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

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
        val targetUser = CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])
        val group = Enums.getIfPresent(Group::class.java, args[1])
        val server = CoreProvider.Cache.Local.SERVERS.provide().fetchByName(args[2])
        val dueAt = DateTime.now() + TimeUnit.DAYS.toMillis(args[3].toLongOrNull() ?: 0)

        if (targetUser === null) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
            return false
        }

        if (group === null || !group.isPresent) {
            commandSender.sendMessage(TextComponent("§cGrupo não localizado"))
            return false
        }

        if ((user!!.getHighestGroup().priority!! <= group.get().priority!!) && !CoreConstants.WHITELISTED_USERS.contains(user.name)) {
            commandSender.sendMessage(TextComponent("§cVocê não pode gerenciar este grupo."))
            return false
        }

        CoreProvider.Repositories.Postgres.USERS_GROUPS_DUE_REPOSITORY.provide().create(
                CreateUserGroupDueDTO(
                        targetUser.id,
                        group.get(),
                        server,
                        dueAt
                )
        )

        commandSender.sendMessage(TextComponent("§aVocê adicionou o grupo ${group.get().displayName} para o usuário ${targetUser.name}."))
        return false
    }

}