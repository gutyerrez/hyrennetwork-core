package com.redefantasy.core.shared.commands

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.CommandRestrictable
import com.redefantasy.core.shared.misc.utils.ChatColor
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.table.UsersTable
import com.redefantasy.core.shared.wrapper.CoreWrapper
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.jetbrains.exposed.dao.id.EntityID
import java.util.stream.Collectors
import kotlin.reflect.jvm.javaMethod

/**
 * @author SrGutyerrez
 **/
interface Commandable<T> {

    fun getName(): String

    fun getDescription(): String? = null

    fun getUsage(): Array<BaseComponent> {
        val arguments = this.getArguments()?.stream()?.map {
            "<${it.name}>"
        }?.distinct()?.collect(Collectors.joining(" ")) ?: ""

        return ComponentBuilder("${ChatColor.RED}Utilize /${this.getNameExact()} $arguments.").create()
    }

    fun getAliases(): Array<String>

    fun getParent(): Commandable<T>? = null

    fun getCommandRestriction(): CommandRestriction? = null

    fun getSubCommands(): List<Commandable<T>>? = null

    fun getArguments(): List<Argument>? = null

    fun isConsole(commandSender: T): Boolean = false

    fun isPlayer(commandSender: T): Boolean = false

    fun canBeExecuteWithoutLogin(): Boolean = false

    fun getSenderName(commandSender: T): String

    fun onCommand(commandSender: T, user: User?, args: Array<out String>): Boolean? = null

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

        var user: User? = if (this.isConsole(commandSender)) {
            User(
                EntityID(
                    CoreConstants.CONSOLE_UUID,
                    UsersTable
                ),
                "CONSOLE"
            )
        } else null

        if (isPlayer(commandSender)) {
            user = CoreProvider.Cache.Local.USERS.provide().fetchByName(this.getSenderName(commandSender))

            if (user === null && !this.canBeExecuteWithoutLogin()) {
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
            if (args.isNotEmpty() && this.getSubCommands() !== null) {
                val subCommand = this.getSubCommands()!!
                        .stream()
                        .filter {
                            it.getName().contentEquals(args[0]) || it.getAliases().contains(args[0])
                        }
                        .findFirst()
                        .orElse(null)

                return if (subCommand !== null) {
                    subCommand.executeRaw(
                            commandSender,
                            args.copyOfRange(1, args.size)
                    )
                } else {
                    this.sendAvailableCommands(commandSender, args)
                }
            } else if (this::onCommand.javaMethod?.returnType?.equals(null) == true) {
                return this.sendAvailableCommands(commandSender, args)
            } else if (args.isEmpty() && this.getArguments() !== null || this.getArguments() !== null && args.size < this.getArguments()!!.size) {
                return CoreWrapper.WRAPPER.sendMessage(
                        this.getSenderName(commandSender),
                        this.getUsage()
                )
            }

            val result = this.onCommand(commandSender, user, args)

            if (result === null && args.isEmpty() && this.getSubCommands() !== null) {
                return this.sendAvailableCommands(commandSender, args)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun sendAvailableCommands(commandSender: T, args: Array<out String>) {
        if (args.isEmpty() && (
                        this.getArguments() !== null || this.getSubCommands() !== null
                        ) || this.getArguments() !== null && this.getArguments()!!.size > args.size
        ) {
            val componentBuilder = ComponentBuilder("\n")
                    .append("§2Comandos disponíveis:")
                    .append("\n\n")

            if (this.getSubCommands() !== null) {
                this.getSubCommands()!!.forEachIndexed { index, commandable ->
                    if (commandable::onCommand.javaMethod?.returnType?.equals(null) != false) {
                        componentBuilder.append(this.getNameExact(), commandable, index, this.getSubCommands()!!.size)
                    } else {
                        if (commandable.getSubCommands() !== null) {
                            commandable.getSubCommands()!!.forEachIndexed { _index, _commandable ->
                                componentBuilder.append(
                                        "${this.getNameExact()} ${commandable.getName()}",
                                        _commandable,
                                        _index,
                                        commandable.getSubCommands()!!.size
                                )
                            }
                        } else {
                            componentBuilder.append(
                                    this.getNameExact(),
                                    commandable,
                                    index,
                                    this.getSubCommands()!!.size
                            )
                        }
                    }
                }
            } else if (this.getArguments() !== null) {
                val arguments = this.getArguments()!!.stream().map {
                    "<${it.name}>"
                }.distinct().collect(Collectors.joining(" "))

                componentBuilder.append("§a/${this.getNameExact()} $arguments §8- §7${this.getDescription() ?: ""}")
                        .event(
                                ClickEvent(
                                        ClickEvent.Action.SUGGEST_COMMAND,
                                        "/${this.getNameExact()} "
                                )
                        )
            } else {
                componentBuilder.append("§a/${this.getNameExact()} §8- §7${this.getDescription() ?: ""}")
                        .event(
                                ClickEvent(
                                        ClickEvent.Action.SUGGEST_COMMAND,
                                        "/${this.getNameExact()} "
                                )
                        )
            }

            componentBuilder.append("\n")

            CoreWrapper.WRAPPER.sendMessage(
                    this.getSenderName(commandSender),
                    componentBuilder.create()
            )

            return
        }

        return
    }

    private fun getNameExact() = if (this.getParent() !== null) {
        var joiner = mutableListOf<String>()

        var parent: Commandable<T>? = null
        var i = 0

        do {
            parent = if (i == 0) this.getParent() else parent?.getParent()

            if (parent !== null) joiner.add(parent.getName())

            i++
        } while (parent !== null)

        "${joiner.reversed().joinToString(" ")} ${this.getName()}"
    } else this.getName()

    private fun ComponentBuilder.append(commandName: String, commandable: Commandable<*>, index: Int, max: Int) {
        if (commandable.getArguments() !== null) {
            val arguments = commandable.getArguments()!!.stream().map { argument ->
                "<${argument.name}>"
            }.distinct().collect(Collectors.joining(" "))

            this.append("§a/$commandName ${commandable.getName()} $arguments §8- §7${commandable.getDescription() ?: ""}")
                    .event(
                            ClickEvent(
                                    ClickEvent.Action.SUGGEST_COMMAND,
                                    "/$commandName ${commandable.getName()} "
                            )
                    )

            if (index + 1 < max) this.append("\n")
        } else {
            this.append("§a/$commandName ${commandable.getName()} §8- §7${commandable.getDescription() ?: ""}")
                    .event(
                            ClickEvent(
                                    ClickEvent.Action.SUGGEST_COMMAND,
                                    "/$commandName ${commandable.getName()} "
                            )
                    )

            if (index + 1 < max) this.append("\n")
        }
    }

    fun <T : Any, R> whenNotNull(input: T?, callback: (T) -> R): R? {
        return input?.let(callback)
    }

}