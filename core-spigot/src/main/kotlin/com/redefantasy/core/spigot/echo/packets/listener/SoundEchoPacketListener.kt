package com.redefantasy.core.spigot.echo.packets.listener

import com.google.common.base.Enums
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.SoundPacket
import org.bukkit.Bukkit
import org.bukkit.Sound
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

                println(sound.name)

                val _sound = Enums.getIfPresent(Sound::class.java, sound.name)

                println("Dps -> ${_sound.isPresent}")

                player.playSound(
                    player.location,
                    sound.name,
                    1f,
                    1f
                )
            }
        }
    }

}