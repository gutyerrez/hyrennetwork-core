package com.redefantasy.core.bungee.command.defaults.staff

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.implementations.GroupCommandRestrictable
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.utils.ChatColor
import com.redefantasy.core.shared.misc.utils.DateFormatter
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder
import java.util.stream.Collectors

/**
 * @author Gutyerrez
 */
class FindCommand : CustomCommand("find"), GroupCommandRestrictable {

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getDescription() = "Ver informações sobre um usuário."

    override fun getArguments() = listOf(
        Argument("usuário")
    )

    override fun getGroup() = Group.MANAGER

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val targetUser = CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])

        if (targetUser === null) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
            return false
        }

        val color = if (targetUser.isOnline()) ChatColor.GREEN else ChatColor.RED

        val address = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchConnectedAddress(targetUser)

        val message = ComponentBuilder()
            .append("\n")
            .append("§2Informações sobre o usuário ${targetUser.name}:")
            .append("\n\n")
            .append(" ${color}Informações básicas")
            .append("\n\n")
            .append("   Id: ${targetUser.getUniqueId()}")
            .append("\n")
            .append("   Data de registro: ${DateFormatter.formatToDefault(targetUser.createdAt)}")
            .append("\n")
            .append("   Última autenticação: ${DateFormatter.formatToDefault(targetUser.lastLoginAt)}")
            .append("\n")
            .append("   Punido: §c--/--")
            .append("\n\n")
            .append(" ${color}Informações avançadas:")
            .append("\n\n")
            .append("   Endereço conectado: ${if (address === null || address.isEmpty()) "§7[Desconhecido]" else address}")
            .append("\n")
            .append("   Último IP: ${if (user!!.hasGroup(Group.MANAGER)) targetUser.lastAddress else "§c[Sem permissão]"}")
            .append("\n\n")
            .append(" ${color}Associações:")
            .append("\n\n")
            .append("   E-mail: §7[Não implementado]")
            .append("\n")
            .append("   Discord: §7[Não implementado]")
            .append("\n")
            .append("   Twitter: §7[Não implementado]")
            .append("\n\n")
            .append(" ${color}Servidores:")
            .append("\n\n")

        val groups =
            CoreProvider.Cache.Local.USERS_GROUPS_DUE.provide().fetchByUserId(targetUser.getUniqueId()) ?: emptyMap()

        groups.forEach {
            val server = it.key
            val _groups = it.value

            val groupsToString = _groups.stream().map { group -> group.displayName }.collect(Collectors.joining(", "))

            message.append("   ${if (server === null) "Todos" else server.displayName}: $groupsToString")
                .append("\n")
        }

        val proxyApplication = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchProxyApplication(targetUser)
        val bukkitApplication = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchBukkitApplication(targetUser)

        message.append("\n\n")
            .append(" ${color}Conexão:")
            .append("\n\n")
            .append("   Conectado: ${if (targetUser.isOnline()) "§aSim" else "§cNão"}")
            .append("\n")
            .append("   Proxy: ${if (proxyApplication === null) "§7[Desconhecido]" else proxyApplication.displayName}")
            .append("\n")
            .append("   Servidor: ${if (bukkitApplication === null || bukkitApplication.server === null) "§7[Desconhecido]" else bukkitApplication.server!!.displayName}")
            .append("\n")

        commandSender.sendMessage(*message.create())
        return true
    }

}