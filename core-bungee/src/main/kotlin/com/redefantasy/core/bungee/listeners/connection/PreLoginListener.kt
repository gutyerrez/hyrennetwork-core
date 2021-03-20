package com.redefantasy.core.bungee.listeners.connection

import com.redefantasy.core.shared.CoreProvider
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.event.PreLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

/**
 * @author Gutyerrez
 */
class PreLoginListener : Listener {

    @EventHandler
    fun on(
        event: PreLoginEvent
    ) {
        val connection = event.connection
        val name = connection.name
        val uuid = connection.uniqueId

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(uuid) ?: CoreProvider.Cache.Local.USERS.provide()
            .fetchByName(name)

        if (user !== null && user.isOnline()) {
            event.setCancelReason(
                *ComponentBuilder("§c§lREDE FANTASY")
                    .append("\n\n")
                    .append("§cJá existe um usuário com o nick ${user.name} online.")
                    .create()
            )
            event.isCancelled = true
        }
    }

}