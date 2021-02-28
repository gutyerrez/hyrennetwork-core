package com.redefantasy.core.bungee.misc.punish.packets.listeners

import com.redefantasy.core.bungee.misc.punish.packets.UserPunishedPacket
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.misc.utils.TimeCode
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ComponentBuilder
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class UserPunishedEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
        packet: UserPunishedPacket
    ) {
        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(packet.userId!!)
        val staffer = CoreProvider.Cache.Local.USERS.provide().fetchById(packet.stafferId!!)
        val punishType = packet.punishType
        val punishDuration = packet.punishDuration
        val punishCategory = CoreProvider.Cache.Local.PUNISH_CATEGORIES.provide().fetchByName(
            packet.punishCategoryName ?: ""
        )

        val message = ComponentBuilder("\n")
            .append("§c * ${user?.name} foi ${punishType?.sampleName} por ${staffer?.name}.")
            .append("\n")
            .append("§c * Motivo: ${punishCategory?.displayName}")
            .append("\n")
            .append("§c * Duração: ${TimeCode.toText(punishDuration, 1)}")
            .create()

        ProxyServer.getInstance().broadcast(*message)
    }

}