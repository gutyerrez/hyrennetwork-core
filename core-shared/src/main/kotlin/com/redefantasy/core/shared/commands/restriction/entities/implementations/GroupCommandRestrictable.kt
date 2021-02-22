package com.redefantasy.core.shared.commands.restriction.entities.implementations

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.commands.restriction.entities.CommandRestrictable
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.users.data.User
import net.md_5.bungee.api.chat.ComponentBuilder

/**
 * @author SrGutyerrez
 **/
interface GroupCommandRestrictable : CommandRestrictable {

    fun getGroup(): Group

    fun strictGroup(): Boolean = false

    override fun canExecute(user: User?): Boolean {
        if (user == null) return false

        val server = CoreProvider.application.server

        return if (this.strictGroup()) {
            user.hasStrictGroup(this.getGroup(), server)
        } else {
            user.hasGroup(this.getGroup(), server)
        }
    }

    override fun getErrorMessage() = ComponentBuilder(
        "§cÉ necessário o grupo ${this.getGroup().displayName} ou superior para executar este comando."
    ).create()

}