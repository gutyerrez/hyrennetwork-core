package com.redefantasy.core.shared.commands

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.commands.restriction.entities.CommandRestrictable
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.table.UsersTable
import com.redefantasy.core.shared.wrapper.CoreWrapper
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import okhttp3.internal.immutableListOf
import org.jetbrains.exposed.dao.id.EntityID
import java.util.stream.Collectors
import kotlin.reflect.jvm.javaMethod

/**
 * @author SrGutyerrez
 **/
interface Commandable<T> {

	fun getName(): String

	fun getDescription0(): String? = null

	fun getUsage0(): Array<BaseComponent> {
		val arguments = this.getArguments()?.stream()?.map {
			"<${it.name}>"
		}?.distinct()?.collect(Collectors.joining(" ")) ?: ""

		return ComponentBuilder("§cUtilize /${this.getNameExact()} $arguments.").create()
	}

	fun getAliases0(): Array<String>

	fun getParent(): Commandable<T>? = null

	fun getCommandRestriction(): CommandRestriction? = null

	fun getSubCommands(): List<Commandable<T>>? = null

	fun getArguments(): List<Argument>? = null

	fun isConsole(
		commandSender: T
	): Boolean = false

	fun isPlayer(
		commandSender: T
	): Boolean = false

	fun canBeExecuteWithoutLogin(): Boolean = false

	fun getSenderName(
		commandSender: T
	): String

	fun onCommand(
		commandSender: T,
		user: User?,
		args: Array<out String>
	): Boolean? = null

	fun executeRaw(
		commandSender: T,
		args: Array<out String>
	) {
		if (this.getCommandRestriction() !== null) {
			if (this.getCommandRestriction() === CommandRestriction.CONSOLE && !this.isConsole(commandSender)) {
				CoreWrapper.WRAPPER.sendMessage(
					commandSender.getName(),
					TextComponent("§cEste comando só pode ser executado por console.")
				)
				return
			}

			if (this.getCommandRestriction() === CommandRestriction.GAME && !this.isPlayer(commandSender)) {
				CoreWrapper.WRAPPER.sendMessage(
					commandSender.getName(),
					TextComponent("§cEste comando só pode ser executado por jogadores.")
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

		if (this.isPlayer(commandSender)) {
			user = CoreProvider.Cache.Local.USERS.provide().fetchByName(commandSender.getName())

			if (user === null && !this.canBeExecuteWithoutLogin()) {
				CoreWrapper.WRAPPER.sendMessage(
					commandSender.getName(),
					TextComponent("§cVocê não está registrado.")
				)
				return
			}

			if (this is CommandRestrictable) {
				if (!this.canExecute(user)) {
					CoreWrapper.WRAPPER.sendMessage(
						commandSender.getName(),
						this.getErrorMessage()
					)
					return
				}
			}
		}

		try {
			if (args.isEmpty() && this.getArguments() !== null || this.getArguments() !== null && args.size < this.getArguments()!!.size) {
				return CoreWrapper.WRAPPER.sendMessage(
					commandSender.getName(),
					this.getUsage0()
				)
			} else if (args.isNotEmpty() && this.getArguments() !== null) {
				this.onCommand(commandSender, user, args)
				return
			} else if (args.isNotEmpty() && this.getSubCommands() !== null) {
				val subCommand = this.getSubCommands()!!
					.stream()
					.filter {
						it.getName().contentEquals(args[0]) || it.getAliases0().contains(args[0])
					}
					.findFirst()
					.orElse(null)

				return when {
					subCommand !== null -> {
						subCommand.executeRaw(
							commandSender,
							args.copyOfRange(1, args.size)
						)
					}
					this::onCommand.javaMethod?.returnType?.equals(null) == true -> {
						return this.sendAvailableCommands0(commandSender, args)
					}
					else -> {
						this.onCommand(commandSender, user, args)
						return
					}
				}
			} else if (this::onCommand.javaMethod?.returnType?.equals(null) == true) {
				return this.sendAvailableCommands0(commandSender, args)
			}

			val result = this.onCommand(commandSender, user, args)

			if (result === null && args.isEmpty() && this.getSubCommands() !== null) {
				return this.sendAvailableCommands0(commandSender, args)
			}
		} catch (ex: Exception) {
			ex.printStackTrace()
		}
	}

	fun sendAvailableCommands(
		commandSender: T
	) = sendAvailableCommands0(
		commandSender,
		emptyArray()
	) == Unit

	private fun sendAvailableCommands0(
		commandSender: T,
		args: Array<out String>
	) {
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

				componentBuilder.append("§a/${this.getNameExact()} $arguments §8- §7${this.getDescription0() ?: ""}")
					.event(
						ClickEvent(
							ClickEvent.Action.SUGGEST_COMMAND,
							"/${this.getNameExact()} "
						)
					)
			} else {
				componentBuilder.append("§a/${this.getNameExact()} §8- §7${this.getDescription0() ?: ""}")
					.event(
						ClickEvent(
							ClickEvent.Action.SUGGEST_COMMAND,
							"/${this.getNameExact()} "
						)
					)
			}

			componentBuilder.append("\n")

			CoreWrapper.WRAPPER.sendMessage(
				commandSender.getName(),
				componentBuilder.create()
			)

			return
		} else if (args.isEmpty() && this.getParent() !== null) {
			val parent = this.getParent()!!

			val componentBuilder = ComponentBuilder("\n")
				.append("§2Comandos disponíveis:")
				.append("\n\n")

			if (parent.getSubCommands() !== null) {
				parent.getSubCommands()!!.forEachIndexed { index, commandable ->
					if (commandable::onCommand.javaMethod?.returnType?.equals(null) != false) {
						componentBuilder.append(parent.getNameExact(), commandable, index, parent.getSubCommands()!!.size)
					} else {
						if (commandable.getSubCommands() !== null) {
							commandable.getSubCommands()!!.forEachIndexed { _index, _commandable ->
								componentBuilder.append(
									"${parent.getNameExact()} ${commandable.getName()}",
									_commandable,
									_index,
									commandable.getSubCommands()!!.size
								)
							}
						} else {
							componentBuilder.append(
								parent.getNameExact(),
								commandable,
								index,
								parent.getSubCommands()!!.size
							)
						}
					}
				}
			} else if (parent.getArguments() !== null) {
				val arguments = parent.getArguments()!!.stream().map {
					"<${it.name}>"
				}.distinct().collect(Collectors.joining(" "))

				componentBuilder.append("§a/${parent.getNameExact()} $arguments §8- §7${parent.getDescription0() ?: ""}")
					.event(
						ClickEvent(
							ClickEvent.Action.SUGGEST_COMMAND,
							"/${parent.getNameExact()} "
						)
					)
			} else {
				componentBuilder.append("§a/${parent.getNameExact()} §8- §7${parent.getDescription0() ?: ""}")
					.event(
						ClickEvent(
							ClickEvent.Action.SUGGEST_COMMAND,
							"/${parent.getNameExact()} "
						)
					)
			}

			componentBuilder.append("\n")

			CoreWrapper.WRAPPER.sendMessage(
				commandSender.getName(),
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

			this.append("§a/$commandName ${commandable.getName()} $arguments §8- §7${commandable.getDescription0() ?: ""}")
				.event(
					ClickEvent(
						ClickEvent.Action.SUGGEST_COMMAND,
						"/$commandName ${commandable.getName()} "
					)
				)

			println("Index: ${index + 1}")
			println("Max: $max")

			if (index + 1 < max) {
				println(1)

				this.append("\n")
			} else println(2)
		} else {
			this.append("§a/$commandName ${commandable.getName()} §8- §7${commandable.getDescription0() ?: ""}")
				.event(
					ClickEvent(
						ClickEvent.Action.SUGGEST_COMMAND,
						"/$commandName ${commandable.getName()} "
					)
				)

			if (index + 1 < max) {
				println(3)

				this.append("\n")
			} else println(4)
		}
	}

	fun onTabComplete0(
		commandSender: T,
		args: Array<out String>
	): Iterable<String?> {
		if (args.isNotEmpty()) {
			val index = args.size - 1
			val token = args[index]

			if (token.isNotEmpty()) {
				when {
					this.getSubCommands() !== null -> {
						val subCommand: String? = this.getSubCommands()!!.stream()
							.filter { it.getName().toLowerCase().startsWith(token.trim().toLowerCase()) }
							.findFirst()
							.map { it.getName() }
							.orElse(null)

						if (subCommand === null) return emptyList()

						return immutableListOf(
							*args.copyOfRange(
								1,
								args.size
							),
							subCommand
						)
					}
					this.getArguments() !== null -> {
						val argument: String? = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchUsers()
							.stream()
							.filter {
								CoreProvider.Cache.Local.USERS.provide().fetchById(it)?.name?.toLowerCase()?.startsWith(token.trim()) ?: false
							}
							.map { CoreProvider.Cache.Local.USERS.provide().fetchById(it)?.name }
							.findFirst()
							.orElse(null)

						if (argument === null) return emptyList()

						return immutableListOf(
							*args.copyOfRange(
								1,
								args.size
							),
							argument
						)
					}
				}
			}
		}

		return immutableListOf()
	}

	private fun T.getName(): String {
		return this@Commandable.getSenderName(this)
	}

}

internal inline fun <T : Any, R> whenNotNull(input: T?, callback: (T) -> R): R? {
	return input?.let(callback)
}
