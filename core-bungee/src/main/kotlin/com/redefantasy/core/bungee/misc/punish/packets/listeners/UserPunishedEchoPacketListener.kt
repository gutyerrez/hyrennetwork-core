package com.redefantasy.core.bungee.misc.punish.packets.listeners

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.UserPunishedPacket
import net.md_5.bungee.api.ProxyServer
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class UserPunishedEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
        packet: UserPunishedPacket
    ) {
        val userId = packet.userId
        val message = packet.message

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(userId!!)

        user?.validatePunishments()

        CoreProvider.Cache.Local.USERS_PUNISHMENTS.provide().invalidate(userId)

        ProxyServer.getInstance().broadcast(*message!!)
    }

}