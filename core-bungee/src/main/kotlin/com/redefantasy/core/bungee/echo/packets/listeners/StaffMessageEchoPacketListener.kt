package com.redefantasy.core.bungee.echo.packets.listeners

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.StaffMessagePacket
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.utils.ChatColor
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.ComponentBuilder
import org.greenrobot.eventbus.Subscribe
import java.util.stream.Collectors

/**
 * @author Gutyerrez
 */
class StaffMessageEchoPacketListener : EchoListener {

    @Subscribe
    fun on(
            packet: StaffMessagePacket
    ) {
        val staffers = ProxyServer.getInstance().players.stream()
                .filter {
                    val user = CoreProvider.Cache.Local.USERS.provide().fetchById(it.uniqueId)

                    user !== null && user.hasGroup(Group.HELPER)
                }
                .collect(Collectors.toList())

        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(packet.stafferId!!)
        val group = user!!.getHighestGroup()

        val message = ComponentBuilder()
                .append("§d[S] ")
                .append("${ChatColor.fromHEX(group.color!!)}$group.prefix")
                .append(user.name)
                .append("§d: ")
                .append(packet.message)
                .create()

        staffers.forEach { it.sendMessage(*message) }
    }

}