package net.hyren.core.bungee.wrapper

import net.hyren.core.shared.wrapper.Wrapper
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

/**
 * @author Gutyerrez
 */
class BungeeWrapper : Wrapper<CommandSender> {

    override fun sendMessage(
        senderName: String,
        messages: Array<BaseComponent>
    ) {
        val commandSender = this.findCommandSender(senderName)

        commandSender.sendMessage(
            *messages
        )
    }

    override fun sendMessage(
        senderName: String,
        message: BaseComponent
    ) {
        val commandSender = this.findCommandSender(senderName)

        commandSender.sendMessage(message)
    }

    override fun sendMessage(
        senderName: String,
        message: String
    ) {
        val commandSender = this.findCommandSender(senderName)

        commandSender.sendMessage(
            TextComponent(message)
        )
    }

    private fun findCommandSender(senderName: String): CommandSender {
        if (senderName == "CONSOLE") return ProxyServer.getInstance().console

        return ProxyServer.getInstance().getPlayer(senderName)
    }

}