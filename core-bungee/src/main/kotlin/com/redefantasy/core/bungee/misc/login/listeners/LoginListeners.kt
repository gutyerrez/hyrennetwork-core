package com.redefantasy.core.bungee.misc.login.listeners

import com.redefantasy.core.bungee.CoreBungeeConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.users.storage.dto.UpdateUserByIdDTO
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import org.joda.time.DateTime
import java.net.InetSocketAddress

/**
 * @author Gutyerrez
 */
class LoginListeners : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun on(event: PostLoginEvent) {
        val proxiedPlayer = event.player

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(proxiedPlayer.uniqueId)

        if (user !== null) {
            user.setLogged(false)

            user.lastLoginAt = DateTime.now()
            user.lastAddress = (proxiedPlayer.pendingConnection.socketAddress as InetSocketAddress).address.hostAddress

            CoreProvider.Repositories.Postgres.USERS_REPOSITORY.provide().update(
                UpdateUserByIdDTO(
                    user.id
                ) {
                    it.lastLoginAt = user.lastLoginAt
                    it.lastAddress = user.lastAddress
                }
            )
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun on(event: PlayerDisconnectEvent) {
        val proxiedPlayer = event.player

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(proxiedPlayer.uniqueId)

        if (user !== null) {
            user.setLogged(false)

            CoreProvider.Cache.Redis.USERS_STATUS.provide().delete(user)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun on(event: ChatEvent) {
        val sender = event.sender
        val message = if (event.message.contains(" ")) {
            event.message.split(" ")[0]
        } else event.message

        if (sender !is ProxiedPlayer) return

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(sender.uniqueId)

        if (!CoreBungeeConstants.UNLOGGED_ALLOWED_COMMANDS.stream().anyMatch {
                it.contentEquals(message)
        } && (user === null || !user.isLogged())) {
            event.isCancelled = true

            sender.sendMessage(TextComponent("§cVocê precisa estar autenticado para utilizar o chat."))
        }
    }

}