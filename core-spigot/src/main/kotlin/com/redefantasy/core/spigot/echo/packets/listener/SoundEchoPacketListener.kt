package com.redefantasy.core.spigot.echo.packets.listener

import com.google.common.base.Enums
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.SoundPacket
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
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

                val _packet = PacketPlayOutNamedSoundEffect(
                    sound.name,
                    0.0,
                    60.0,
                    0.0,
                    0f,
                    0f
                )

                val handle = (player as CraftPlayer).handle
                val playerConnection = handle.playerConnection

                playerConnection.sendPacket(_packet)
            }
        }
    }

}