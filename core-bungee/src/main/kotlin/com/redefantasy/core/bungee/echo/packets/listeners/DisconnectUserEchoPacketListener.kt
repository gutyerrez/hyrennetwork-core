package com.redefantasy.core.bungee.echo.packets.listeners

import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.DisconnectUserPacket
import net.md_5.bungee.api.ProxyServer
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class DisconnectUserEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
        packet: DisconnectUserPacket
    ) {
        val userId = packet.userId!!

        val proxiedPlayer = ProxyServer.getInstance().getPlayer(userId)

        if (proxiedPlayer === null) return

        proxiedPlayer.disconnect(*packet.message!!)
    }

}