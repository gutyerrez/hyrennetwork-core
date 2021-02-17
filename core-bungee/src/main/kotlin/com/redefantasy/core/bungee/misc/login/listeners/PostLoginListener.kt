package com.redefantasy.core.bungee.misc.login.listeners

import com.redefantasy.core.shared.CoreProvider
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

/**
 * @author Gutyerrez
 */
class PostLoginListener : Listener {

    @EventHandler
    fun on(event: PostLoginEvent) {
        val proxiedPlayer = event.player

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(proxiedPlayer.uniqueId)

        if (user !== null) {
            user.setLogged(false)

            // TODO
        }
    }

}