package com.redefantasy.core.spigot

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.wrapper.CoreWrapper
import com.redefantasy.core.spigot.command.registry.CommandRegistry
import com.redefantasy.core.spigot.echo.packets.listener.SoundEchoPacketListener
import com.redefantasy.core.spigot.echo.packets.listener.TitleEchoPacketListener
import com.redefantasy.core.spigot.listeners.GenericListener
import com.redefantasy.core.spigot.misc.plugin.CustomPlugin
import com.redefantasy.core.spigot.misc.skin.command.SkinCommand
import com.redefantasy.core.spigot.misc.utils.PacketEvent
import com.redefantasy.core.spigot.misc.utils.PacketListener
import com.redefantasy.core.spigot.wrapper.SpigotWrapper
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateSign
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

    private var onlineSince = 0L

    override fun onEnable() {
        super.onEnable()

        CoreSpigotProvider.prepare()

        this.onlineSince = System.currentTimeMillis()

        CoreWrapper.WRAPPER = SpigotWrapper()

        CoreProvider.Databases.Redis.ECHO.provide().registerListener(TitleEchoPacketListener())
        CoreProvider.Databases.Redis.ECHO.provide().registerListener(SoundEchoPacketListener())

        val pluginManager = Bukkit.getServer().pluginManager

        /**
         * Spigot listeners
         */

        pluginManager.registerEvents(GenericListener(), this)
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

        /**
         * Commands
         */

        CommandRegistry.registerCommand(SkinCommand())

        /**
         * Packets
         */
        CoreSpigotConstants.PROTOCOL_HANDLER.registerListener(
            object : PacketListener() {

                override fun onReceive(
                    event: PacketEvent
                ) {
                    val player = event.player
                    val packet = event.packet

                    if (packet is PacketPlayOutUpdateSign) {
                        event.isCancelled = true

                        println("dale")

                        Bukkit.getScheduler().runTask(
                            this@CoreSpigotPlugin
                        ) {
                            if (player.isOnline) {
                                val location = player.location
                                val block = location.block

                                player.sendBlockChange(
                                    location,
                                    block.type,
                                    block.data
                                )
                            }
                        }
                    }
                }

            }
        )
    }

}