package com.redefantasy.core.shared.commands

import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.commands.restriction.CommandRestriction
import com.redefantasy.core.shared.users.data.User

/**
 * @author SrGutyerrez
 **/
interface Commandable<T> {

    fun getParent(): Commandable<T>? = null

    fun getCommandRestriction(): CommandRestriction? = null

    fun <S: Commandable<T>> getSubCommands(): Map<S, String>? = null

    fun getArguments(): List<Argument>? = null

    fun isConsole(t: T): Boolean = false

    fun isPlayer(t: T): Boolean = false

    fun onCommand(sender: T, user: User, args: List<String>): Boolean

    fun executeRaw(sender: T, vararg args: List<String>) {
        // implement this before...
    }

}