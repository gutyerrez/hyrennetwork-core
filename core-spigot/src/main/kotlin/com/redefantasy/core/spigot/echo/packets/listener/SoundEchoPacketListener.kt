package com.redefantasy.core.spigot.echo.packets.listener

import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.SoundPacket
import org.bukkit.Bukkit
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class SoundEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
        packet: SoundPacket
    ) {
        val sound = packet.sound!!

        println(packet.usersId)

        packet.usersId?.forEach {
            val player = Bukkit.getPlayer(it)

            player.playSound(
                player.location,
                sound.name,
                packet.volume1!!,
                packet.volume2!!
            )
        }
    }

}