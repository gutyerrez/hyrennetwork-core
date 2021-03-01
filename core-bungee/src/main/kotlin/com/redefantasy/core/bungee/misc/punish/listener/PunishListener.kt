package com.redefantasy.core.bungee.misc.punish.listener

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.misc.utils.TimeCode
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

/**
 * @author Gutyerrez
 */
class PunishListener : Listener {

    @EventHandler
    fun on(
        event: PreLoginEvent
    ) {
        val connection = event.connection
        val userId = connection.uniqueId
        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)

        if (user === null) return

        user.validatePunishments()
    }

    @EventHandler
    fun on(
        event: ChatEvent
    ) {
        val proxiedPlayer = event.sender as ProxiedPlayer
        val userId = proxiedPlayer.uniqueId
        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)

        if (user === null ) return

        val currentActiveMutePunishment = user.isMuted()

        if (currentActiveMutePunishment !== null) {
            event.isCancelled = true

            proxiedPlayer.sendMessage(
                *ComponentBuilder("\n")
                    .append("§c * Você foi ${currentActiveMutePunishment.punishType.sampleName} por ${user.name}.")
                    .append("\n")
                    .append("§c * Motivo: ${currentActiveMutePunishment.punishCategory?.displayName}")
                    .append("\n")
                    .append("§c * Duração: ${TimeCode.toText(currentActiveMutePunishment.duration, 1)}")
                    .append("\n")
                    .create()
            )
        }
    }

}