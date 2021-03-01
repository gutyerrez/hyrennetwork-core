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

        packet.usersId?.forEach {
            val player = Bukkit.getPlayer(it)

            println(it)
            println(player === null)

            if (player !== null) {
                println("Tocar pra ele")

                player.playSound(
                    player.location,
                    sound.name,
                    1F,
                    1F
                )
            }
        }
    }

}