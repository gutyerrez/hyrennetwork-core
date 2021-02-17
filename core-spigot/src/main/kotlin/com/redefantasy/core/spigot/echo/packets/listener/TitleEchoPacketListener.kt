package com.redefantasy.core.spigot.echo.packets.listener

import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.packets.TitlePacket
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.greenrobot.eventbus.Subscribe

/**
 * @author Gutyerrez
 */
class TitleEchoPacketListener : EchoListener {

    @Subscribe
    fun on(packet: TitlePacket) {
        Bukkit.broadcast(TextComponent("Testando ${packet.title} | ${packet.subTitle}"))
    }

}