package com.redefantasy.core.shared.commands

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.CommandRestrictable
import com.redefantasy.core.shared.misc.utils.ChatColor
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.wrapper.CoreWrapper
import net.md_5.bungee.api.chat.TextComponent

/**
 * @author SrGutyerrez
 **/
interface Commandable<T> {

    fun getName(): String

    fun getAliases(): Array<String>

    fun getParent(): Commandable<T>? = null

    fun getCommandRestriction(): CommandRestriction? = null

    fun <S : Commandable<T>> getSubCommands(): Map<S, String>? = null

    fun getArguments(): List<Argument>? = null

    fun isConsole(commandSender: T): Boolean = false

    fun isPlayer(commandSender: T): Boolean = false

    fun getSenderName(commandSender: T): String

    fun onCommand(commandSender: T, user: User?, args: Array<out String>): Boolean

    fun executeRaw(commandSender: T, args: Array<out String>) {
        if (this.getCommandRestriction() !== null) {
            if (this.getCommandRestriction() === CommandRestriction.CONSOLE && !this.isConsole(commandSender)) {
                CoreWrapper.WRAPPER.sendMessage(
                    this.getSenderName(commandSender),
                    TextComponent("${ChatColor.RED}Este comando só pode ser executado por console.")
                )
                return
            }

            if (this.getCommandRestriction() === CommandRestriction.GAME && !this.isPlayer(commandSender)) {
                CoreWrapper.WRAPPER.sendMessage(
                    this.getSenderName(commandSender),
                    TextComponent("${ChatColor.RED}Este comando só pode ser executado por jogadores.")
                )
                return
            }
        }

        var user: User? = null

        if (isPlayer(commandSender)) {
            user = CoreProvider.Cache.Local.USERS.provide().fetchByName(this.getSenderName(commandSender))

            if (user === null) {
                CoreWrapper.WRAPPER.sendMessage(
                    this.getSenderName(commandSender),
                    TextComponent("${ChatColor.RED}Você não está registrado.")
                )
                return
            }

            if (this is CommandRestrictable) {
                if (!this.canExecute(user)) {
                    CoreWrapper.WRAPPER.sendMessage(
                        this.getSenderName(commandSender),
                        this.getErrorMessage()
                    )
                    return
                }
            }
        }

        try {
            if (args.isNotEmpty() && this.getSubCommands<Commandable<T>>() !== null) {
                val subCommand = this.getSubCommands<Commandable<T>>()!!
                    .keys
                    .stream()
                    .filter {
                        it.getName() === args[0] || it.getAliases().contains(args[0])
                    }
                    .findFirst()
                    .orElse(null)

                if (subCommand !== null) subCommand.executeRaw(
                    commandSender,
                    args.copyOfRange(1, args.size)
                )
            }

            this.onCommand(commandSender, user, args)
        } catch (e: Exception) {
            // Criar log de erro
        }
    }

}