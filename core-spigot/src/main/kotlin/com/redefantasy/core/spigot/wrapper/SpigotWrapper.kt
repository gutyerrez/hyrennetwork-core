package com.redefantasy.core.spigot.wrapper

import com.redefantasy.core.shared.wrapper.Wrapper
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class SpigotWrapper : Wrapper<CommandSender> {

    override fun sendMessage(
        senderName: String,
        messages: Array<BaseComponent>
    ) {
        val commandSender = this.findCommandSender(senderName)

        if (commandSender is Player) {
            commandSender.sendMessage(
                messages
            )
        } else {
            messages.forEach {
                commandSender.sendMessage(it.toLegacyText())
            }
        }
    }

    override fun sendMessage(
        senderName: String,
        message: BaseComponent
    ) {
        val commandSender = this.findCommandSender(senderName)

        if (commandSender is Player) {
            commandSender.sendMessage(
                message
            )
        } else {
            commandSender.sendMessage(message.toLegacyText())
        }
    }

    override fun sendMessage(
        senderName: String,
        message: String
    ) {
        val commandSender = this.findCommandSender(senderName)

        if (commandSender is Player) {
            commandSender.sendMessage(
                message
            )
        }
    }

    private fun findCommandSender(senderName: String): CommandSender {
        if (senderName === "CONSOLE") return Bukkit.getConsoleSender()

        return Bukkit.getPlayerExact(senderName)
    }

}