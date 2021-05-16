package net.hyren.core.spigot.echo.packets.listener

import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.core.shared.echo.packets.SoundPacket
import org.bukkit.Bukkit
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class SoundEchoPacketListener : EchoPacketListener {

    @Subscribe
    fun on(
        packet: SoundPacket
    ) {
        val sound = packet.sound!!

        packet.usersId?.forEach {
            val player = Bukkit.getPlayer(it)

            if (player !== null) player.playSound(
                player.location,
                sound.name,
                packet.volume2!!,
                packet.volume1!!
            )
        }
    }

}