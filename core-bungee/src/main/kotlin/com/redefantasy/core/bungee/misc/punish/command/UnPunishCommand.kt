package com.redefantasy.core.bungee.misc.punish.command

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.misc.punish.packets.UserUnPunishedPacket
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.punishments.storage.dto.UpdateUserPunishmentByIdDTO
import com.redefantasy.core.shared.users.punishments.storage.table.UsersPunishmentsTable
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class UnPunishCommand : CustomCommand("despunir") {

    override fun getDescription() = "Revogar uma punição."

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getArguments() = listOf(
        Argument("id"),
        Argument("motivo")
    )

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        val userPunishment = CoreProvider.Cache.Local.USERS_PUNISHMENTS.provide().fetchById(
            EntityID(args[0].toIntOrNull() ?: 0, UsersPunishmentsTable)
        )
        val revokeCategory = CoreProvider.Cache.Local.REVOKE_CATEGORIES.provide().fetchByName(args[1])

        if (userPunishment === null) {
            commandSender.sendMessage(TextComponent("§cEsta punição não existe."))
            return false
        }

        if (revokeCategory === null) {
            commandSender.sendMessage(TextComponent("§cMotivo de revogar inválido."))
            return false
        }

        if (!userPunishment.isActive()) {
            commandSender.sendMessage(TextComponent("§cEsta punição não está mais ativa."))
            return false
        }

        if (!userPunishment.canBeRevokedFrom(user!!) || userPunishment.punishCategory !== null && !user.hasGroup(
                userPunishment.punishCategory!!.group
            )
        ) {
            commandSender.sendMessage(TextComponent("§cVocê não pode revogar esta punição."))
            return false
        }

        CoreProvider.Repositories.Postgres.USERS_PUNISHMENTS_REPOSITORY.provide().update(
            UpdateUserPunishmentByIdDTO(
                userPunishment.id
            ) {
                it.revokeStafferId = user.id
                it.revokeTime = DateTime.now()
                it.revokeCategory = revokeCategory.name
            }
        )

        val punisherUser = CoreProvider.Cache.Local.USERS.provide().fetchById(userPunishment.stafferId)
        val punishedUser = CoreProvider.Cache.Local.USERS.provide().fetchById(userPunishment.userId)

        val message = ComponentBuilder()
            .append("\n")
            .append("§e * ${user!!.name} revogou uma punição de ${punishedUser!!.name}.")
            .append("\n")
            .append("§e * Aplicada inicialmente por ${punisherUser!!.name}.")
            .append("\n")
            .append("§e * Motivo: ${revokeCategory.displayName}.")
            .append("\n")
            .create()

        val packet = UserUnPunishedPacket()

        packet.userId = punishedUser.getUniqueId()
        packet.message = message

        val proxyApplications = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(ApplicationType.PROXY)

        CoreProvider.Databases.Redis.ECHO.provide().publishToApplications(
            packet,
            proxyApplications
        )

        commandSender.sendMessage(TextComponent("§eVocê revogou a punição §b#${userPunishment.id.value} por ${revokeCategory.displayName}."))
        return true
    }

}