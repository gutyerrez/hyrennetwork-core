package net.hyren.core.spigot

import net.hyren.core.shared.CoreProviderimport net.hyren.core.shared.wrapper.CoreWrapperimport net.hyren.core.spigot.echo.packets.listener.SoundEchoPacketListenerimport net.hyren.core.spigot.echo.packets.listener.TitleEchoPacketListenerimport net.hyren.core.spigot.listeners.GenericListenerimport net.hyren.core.spigot.misc.plugin.CustomPluginimport net.hyren.core.spigot.misc.utils.PacketEventimport net.hyren.core.spigot.misc.utils.PacketListenerimport net.hyren.core.spigot.sign.CustomSignimport net.hyren.core.spigot.wrapper.SpigotWrapperimport net.minecraft.server.v1_8_R3.PacketPlayInUpdateSignimport org.bukkit.Bukkitimport org.bukkit.event.EventHandlerimport org.bukkit.event.Listenerimport org.bukkit.event.player.PlayerJoinEventimport org.bukkit.event.player.PlayerQuitEvent

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
         * Protocol
         */

        CoreSpigotConstants.PROTOCOL_HANDLER?.registerListener(
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