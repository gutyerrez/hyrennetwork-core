package net.hyren.core.bungee.echo.packet.listener

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.echo.api.listener.EchoListener
import net.hyren.core.shared.echo.packets.ConnectUserToApplicationPacket
import net.md_5.bungee.api.ProxyServer
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class ConnectUserToApplicationEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
        packet: ConnectUserToApplicationPacket
    ) {
        val userId = packet.userId!!
        val application = packet.application!!

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)

        if (user === null) return

        val proxiedPlayer = ProxyServer.getInstance().getPlayer(user.getUniqueId())

        if (proxiedPlayer === null) return

        proxiedPlayer.connect { application.address }
    }

}