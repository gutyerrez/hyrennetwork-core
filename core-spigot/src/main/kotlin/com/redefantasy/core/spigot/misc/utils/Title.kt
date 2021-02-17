package com.redefantasy.core.spigot.misc.utils

import com.redefantasy.core.shared.misc.utils.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
data class Title(
    var title: String?,
    var subTitle: String?,
    val fadeIn: Long,
    val fadeOut: Long,
    val stay: Long
) {

    fun sendToPlayer(player: Player) {
        if (title !== null) {
            title = ChatColor.translateAlternateColorCodes('&', title!!)

            var `object` = this.getNMSClass("PacketPlayOutTitle").declaredClasses[0].getField("TIMES").get(null)
            var chatTitle = this.getNMSClass("IChatBaseComponent").declaredClasses[0].getMethod("a", String::class.java)
                .invoke(null, "{\"text\":\"$title\"}")
            var subTitleConstructor = this.getNMSClass("PacketPlayOutTitle").getConstructor(
                this.getNMSClass("PacketPlayOutTitle").declaredClasses[0],
                this.getNMSClass("IChatBaseComponent"),
                Integer.TYPE,
                Integer.TYPE,
                Integer.TYPE
            )
            var titlePacket = subTitleConstructor.newInstance(`object`, chatTitle, fadeIn, stay, fadeOut)

            player.sendPacket(titlePacket)

            `object` = this.getNMSClass("PacketPlayOutTitle").declaredClasses[0].getField("TITLE").get(null)
            chatTitle = this.getNMSClass("IChatBaseComponent").declaredClasses[0].getMethod("a", String::class.java)
                .invoke(null, "{\"text\": \"$title\"}")
            subTitleConstructor = this.getNMSClass("PacketPlayOutTitle").getConstructor(
                this.getNMSClass("PacketPlayOutTitle").declaredClasses[0],
                this.getNMSClass("IChatBaseComponent")
            )
            titlePacket = subTitleConstructor.newInstance(`object`, chatTitle)

            player.sendPacket(titlePacket)
        }

        if (subTitle !== null) {
            var `object` = this.getNMSClass("PacketPlayOutTitle").declaredClasses[0].getField("TIMES").get(null)
            var chatTitle = this.getNMSClass("IChatBaseComponent").declaredClasses[0].getMethod("a", String::class.java)
                .invoke(null, "{\"text\":\"$title\"}")
            var subTitleConstructor = this.getNMSClass("PacketPlayOutTitle").getConstructor(
                this.getNMSClass("PacketPlayOutTitle").declaredClasses[0],
                this.getNMSClass("IChatBaseComponent"),
                Integer.TYPE,
                Integer.TYPE,
                Integer.TYPE
            )
            var titlePacket = subTitleConstructor.newInstance(`object`, chatTitle, fadeIn, stay, fadeOut)

            player.sendPacket(titlePacket)

            `object` = this.getNMSClass("PacketPlayOutTitle").declaredClasses[0].getField("SUBTITLE").get(null)
            chatTitle = this.getNMSClass("IChatBaseComponent").declaredClasses[0].getMethod("a", String::class.java)
                .invoke(null, "{\"text\": \"$title\"}")
            subTitleConstructor = this.getNMSClass("PacketPlayOutTitle").getConstructor(
                this.getNMSClass("PacketPlayOutTitle").declaredClasses[0],
                this.getNMSClass("IChatBaseComponent"),
                Integer.TYPE,
                Integer.TYPE,
                Integer.TYPE
            )
            titlePacket = subTitleConstructor.newInstance(`object`, chatTitle)

            player.sendPacket(titlePacket)

        }
    }

    private fun Player.sendPacket(packet: Any) {
        val handle = this::class.java.getMethod("getHandle", *arrayOf<Class<*>>()).invoke(player, Any())
        val playerConnection = handle::class.java.getField("playerConnection").get(handle)

        playerConnection::class.java.getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet)
    }

    private fun getNMSClass(name: String): Class<*> {
        println(Bukkit.getServer()::class.java.`package`.name.split("\\\\."))

        val version = Bukkit.getServer()::class.java.`package`.name.split("\\\\.")[3]

        return Class.forName("net.minecraft.server.$version.$name")
    }

}