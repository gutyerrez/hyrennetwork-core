package com.redefantasy.core.shared.commands.restriction.entities

import com.redefantasy.core.shared.users.data.User

/**
 * @author SrGutyerrez
 **/
interface CommandRestrictable {

    fun canExecute(user: User?): Boolean

    fun getErrorMessage(): String

}