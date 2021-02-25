package com.redefantasy.core.bungee.misc.punish.packets.listeners

import com.redefantasy.core.bungee.misc.punish.packets.UserUnPunishedPacket
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import net.md_5.bungee.api.ProxyServer
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class UserUnPunishedEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
            packet: UserUnPunishedPacket
    ) {
        val userId = packet.userId!!
        val message = packet.message!!

        CoreProvider.Cache.Local.USERS_PUNISHMENTS.provide().invalidate(userId)

        ProxyServer.getInstance().broadcast(*message)
    }

}