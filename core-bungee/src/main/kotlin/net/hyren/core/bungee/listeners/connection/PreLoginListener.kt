package net.hyren.core.bungee.listeners.connection

import net.hyren.core.shared.CoreConstants
import net.hyren.core.shared.CoreProvider
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

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(uuid) ?: CoreProvider.Cache.Local.USERS.provide().fetchByName(name)

        if (user?.getUniqueId() == CoreConstants.CONSOLE_UUID) {
            event.setCancelReason(
                *ComponentBuilder(CoreConstants.Info.ERROR_SERVER_NAME)
                    .append("\n\n")
                    .append("§cA conta ${user?.name} não pode jogar.")
                    .create()
            )
            event.isCancelled = true
            return
        }

        if (user !== null && user.isOnline()) {
            event.setCancelReason(
                *ComponentBuilder(CoreConstants.Info.ERROR_SERVER_NAME)
                    .append("\n\n")
                    .append("§cJá existe um usuário com o nome ${user.name} online.")
                    .create()
            )
            event.isCancelled = true
        }
    }

}