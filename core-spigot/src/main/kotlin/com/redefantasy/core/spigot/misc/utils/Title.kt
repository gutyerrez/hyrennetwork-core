package com.redefantasy.core.spigot.misc.utils

import net.md_5.bungee.api.chat.TextComponent
import net.minecraft.server.v1_8_R3.Packet
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class Title(
    var title: String?,
    var subTitle: String?,
    val fadeIn: Long,
    val fadeOut: Long,
    val stay: Long
) {

    fun sendToPlayer(player: Player) {
        if (title !== null) {
            val title = PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.TITLE,
                arrayOf(
                    TextComponent(title)
                ),
                fadeIn.toInt(),
                stay.toInt(),
                fadeOut.toInt()
            )

            player.sendPacket(title)
        }
    }

    private fun Player.sendPacket(packet: Packet<*>) {
        val handle = (this as CraftPlayer).handle
        val playerConnection = handle.playerConnection

        playerConnection.sendPacket(packet)
//        val handle = this::class.java.getMethod("getHandle", *arrayOf<Class<*>>()).invoke(player, Any())
//        val playerConnection = handle::class.java.getField("playerConnection").get(handle)
//
//        playerConnection::class.java.getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet)
    }

    private fun getNMSClass(name: String): Class<*> {
        val version = Bukkit.getServer()::class.java.`package`.name.split(".")[3]

        return Class.forName("net.minecraft.server.$version.$name")
    }

}