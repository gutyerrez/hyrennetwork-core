package com.redefantasy.core.bungee.misc.punish.listener

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.misc.punish.PunishType
import com.redefantasy.core.shared.users.punishments.storage.dto.UpdateUserPunishmentByIdDTO
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class PunishListener : Listener {

    @EventHandler
    fun on(
        event: PreLoginEvent
    ) {
        val connection = event.connection

        println(connection.name)
        println(connection.uniqueId)

        val userId = connection.uniqueId
        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)
        val userPunishments = user?.getPunishments() ?: emptyList()

        userPunishments.forEach {
            if (it.startTime === null) {
                it.startTime = DateTime.now()

                CoreProvider.Repositories.Postgres.USERS_PUNISHMENTS_REPOSITORY.provide().update(
                    UpdateUserPunishmentByIdDTO(
                        it.id
                    ) { userPunishmentDAO ->
                        userPunishmentDAO.startTime = it.startTime
                    }
                )
            }
        }

        val activePunishment = userPunishments.stream().filter {
            it.isActive() && it.punishType !== PunishType.MUTE
        }.findFirst().orElse(null)

        if (activePunishment !== null) {
            val staffer = CoreProvider.Cache.Local.USERS.provide().fetchById(activePunishment.stafferId)

            connection.disconnect(
                *ComponentBuilder()
                    .append("§c§lREDE FANTASY")
                    .append("\n\n")
                    .append("§cVocê está ${activePunishment.punishType.sampleName} do servidor")
                    .append("\n\n")
                    .append("§cMotivo: ${activePunishment.punishCategory?.displayName ?: activePunishment.customReason} - ${activePunishment.proof}")
                    .append("\n")
                    .append("§cAutor: ${staffer?.name}")
                    .append("\n\n")
                    .append("§cUse o ID §b#${activePunishment.id.value} §cpara criar uma revisão em &mdiscord.gg/redefantasy§r§c.")
                    .create()
            )
            return
        }
    }

}