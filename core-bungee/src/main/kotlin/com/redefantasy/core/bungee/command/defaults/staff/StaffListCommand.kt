package com.redefantasy.core.bungee.command.defaults.staff

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.implementations.GroupCommandRestrictable
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.utils.ChatColor
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder
import java.util.stream.Collectors

/**
 * @author Gutyerrez
 */
class StaffListCommand : CustomCommand("staff"), GroupCommandRestrictable {

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getDescription() = "Ver a lista de membros da equipe."

    override fun getGroup() = Group.HELPER

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val users = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsers()
            .stream()
            .map {
                val _user = CoreProvider.Cache.Local.USERS.provide().fetchById(it)

                _user
            }
            .filter { it !== null && it.hasGroup(Group.HELPER) }
            .collect(Collectors.toList())

        val message = ComponentBuilder()
            .append("\n")
            .append("§2Membros da equipe online (${users.size}):")
            .append("\n\n")

        users.stream()
            .sorted { user1, user2 -> user2!!.getHighestGroup().priority!!.compareTo(user1!!.getHighestGroup().priority!!) }
            .forEach {
                val highestGroup = it!!.getHighestGroup()
                val prefix = "${ChatColor.fromHEX(highestGroup.color!!)}${highestGroup.prefix}"
                val bukkitApplication = it.getConnectedBukkitApplication()

                message.append(
                    "$prefix${it.name} §7(${if (bukkitApplication === null) "Desconhecido" else bukkitApplication.displayName})"
                ).append("\n")
            }

        commandSender.sendMessage(*message.create())
        return false
    }

}