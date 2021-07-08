package net.hyren.core.spigot

import com.scuti.blockexploits.spigot.BlockExploitsPlugin
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.wrapper.CoreWrapper
import net.hyren.core.spigot.echo.packets.listener.SoundEchoPacketListener
import net.hyren.core.spigot.echo.packets.listener.TitleEchoPacketListener
import net.hyren.core.spigot.listeners.GenericListener
import net.hyren.core.spigot.misc.plugin.CustomPlugin
import net.hyren.core.spigot.misc.utils.PacketEvent
import net.hyren.core.spigot.misc.utils.PacketListener
import net.hyren.core.spigot.sign.CustomSign
import net.hyren.core.spigot.wrapper.SpigotWrapper
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign
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
         * Scuti Block Exploits
         */

        BlockExploitsPlugin.getInstance().onEnable()

        /**
         * Protocol
         */

        CoreSpigotConstants.PROTOCOL_HANDLER.registerListener(
            object : PacketListener() {

                override fun onReceive(
                    event: PacketEvent
                ) {
                    val player = event.player
                    val packet = event.packet

                    if (packet is PacketPlayInUpdateSign) {
                        if (player.hasMetadata("custom-sign")) {
                            val customSign = player.getMetadata("custom-sign")[0].value() as CustomSign
                            val texts = packet.b()

                            customSign.UPDATED_LISTENER?.accept(
                                player,
                                texts
                            )

                            player.removeMetadata("custom-sign", this@CoreSpigotPlugin)
                        }
                    }
                }

            }
        )
    }

}