package com.redefantasy.core.bungee.echo.packets.listeners

import com.redefantasy.core.bungee.echo.packets.TellPacket
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ComponentBuilder

/**
 * @author Gutyerrez
 */
class TellPacketListener : EchoListener {

    fun on(
            packet: TellPacket
    ) {
        val senderId = packet.senderId
        val receiverId = packet.receiverId
        val message = packet.message

        val sender = CoreProvider.Cache.Local.USERS.provide().fetchById(senderId!!)!!
        val receiver = CoreProvider.Cache.Local.USERS.provide().fetchById(receiverId!!)!!

        val toMessage = ComponentBuilder()
                .append("§8[Para ${receiver.name}]: §6")
                .append(message)
                .create()

        val fromMessage = ComponentBuilder()
                .append("§8[De ${sender.name}]: §6")
                .append(message)
                .create()

        val senderPlayer = ProxyServer.getInstance().getPlayer(sender.getUniqueId())
        val receiverPlayer = ProxyServer.getInstance().getPlayer(receiver.getUniqueId())

        if (senderPlayer !== null) senderPlayer.sendMessage(*toMessage)

        if (receiverPlayer !== null) receiverPlayer.sendMessage(*fromMessage)
    }

}