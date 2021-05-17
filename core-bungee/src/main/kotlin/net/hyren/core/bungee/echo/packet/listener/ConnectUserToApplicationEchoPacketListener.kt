package net.hyren.core.bungee.echo.packet.listener

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.core.shared.echo.packets.ConnectUserToApplicationPacket
import net.md_5.bungee.api.ProxyServer
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class ConnectUserToApplicationEchoPacketListener : EchoPacketListener {

    @Subscribe
    fun on(
        packet: ConnectUserToApplicationPacket
    ) {
        val userId = packet.userId!!
        val application = packet.application!!

        println("UserId: $userId")

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId)

        println("User id é $user")

        if (user === null) return

        println("User id não é null")

        val proxiedPlayer = ProxyServer.getInstance().getPlayer(user.getUniqueId())

        println("ProxiedPlayer é $proxiedPlayer")

        if (proxiedPlayer === null) return

        println("Connect user to application ${application.address}")

        proxiedPlayer.connect { application.address }
    }

}