package net.hyren.core.spigot.echo.packets.listener

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.echo.api.listener.EchoListener
import net.hyren.core.shared.echo.packets.TitlePacket
import net.hyren.core.spigot.misc.utils.Title
import org.bukkit.Bukkit
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class TitleEchoPacketListener : EchoListener {

    @Subscribe
    fun on(packet: TitlePacket) {
        val title = Title(
            packet.title,
            packet.subTitle,
            packet.fadeIn ?: 0,
            packet.fadeOut ?: 30,
            packet.stay ?: 0
        )

        if (packet.userId === null) throw NullPointerException("User id cannot be null")

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(packet.userId!!)

        if (user === null) throw NullPointerException("User cannot be null")

        val player = Bukkit.getPlayer(user.getUniqueId())

        title.sendToPlayer(player)
    }

}