package com.redefantasy.core.shared.commands.restriction.entities

import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.chat.BaseComponent

/**
 * @author SrGutyerrez
 **/
interface CommandRestrictable {

    fun canExecute(user: User?): Boolean

    fun getErrorMessage(): Array<BaseComponent>

}