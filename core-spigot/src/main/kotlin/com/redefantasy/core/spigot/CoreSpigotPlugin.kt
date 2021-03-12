package com.redefantasy.core.spigot

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.wrapper.CoreWrapper
import com.redefantasy.core.spigot.echo.packets.listener.SoundEchoPacketListener
import com.redefantasy.core.spigot.echo.packets.listener.TitleEchoPacketListener
import com.redefantasy.core.spigot.listeners.GeneralListener
import com.redefantasy.core.spigot.misc.plugin.CustomPlugin
import com.redefantasy.core.spigot.wrapper.SpigotWrapper
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author Gutyerrez
 */
class CoreSpigotPlugin : CustomPlugin(true) {

    companion object {
        lateinit var instance: CustomPlugin
    }

    init {
        instance = this
    }

    override fun onEnable() {
        super.onEnable()

        CoreSpigotProvider.prepare()

        CoreWrapper.WRAPPER = SpigotWrapper()

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(TitleEchoPacketListener())
        CoreProvider.Databases.Redis.ECHO.provide().registerListener(SoundEchoPacketListener())

        val pluginManager = Bukkit.getServer().pluginManager

        pluginManager.registerEvents(GeneralListener(), this)
        pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun on(
                event: PlayerJoinEvent
            ) {
                event.joinMessage = null
            }

            @EventHandler
            fun on(
                event: PlayerQuitEvent
            ) {
                event.quitMessage = null
            }
        }, this)
    }

}