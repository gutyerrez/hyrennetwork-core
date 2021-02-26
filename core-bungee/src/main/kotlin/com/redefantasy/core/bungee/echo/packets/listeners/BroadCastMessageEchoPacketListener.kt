package com.redefantasy.core.bungee.echo.packets.listeners

import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.BroadcastMessagePacket
import net.md_5.bungee.api.ProxyServer
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class BroadCastMessageEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
        packet: BroadcastMessagePacket
    ) {
        ProxyServer.getInstance().broadcast(*packet.message!!)
    }

}