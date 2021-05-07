package net.hyren.core.shared.commands.restriction.entities

import net.hyren.core.shared.users.data.User
import net.md_5.bungee.api.chat.BaseComponent

/**
 * @author SrGutyerrez
 **/
interface CommandRestrictable {

    fun canExecute(user: User?): Boolean

    fun getErrorMessage(): Array<BaseComponent>

}