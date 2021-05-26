package net.hyren.core.shared.commands.restriction.entities.implementations

import net.hyren.core.shared.commands.restriction.entities.CommandRestrictable
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.users.data.User
import net.md_5.bungee.api.chat.ComponentBuilder

/**
 * @author SrGutyerrez
 **/
interface GroupCommandRestrictable : CommandRestrictable {

    fun getServer(): Server? = null

    fun getGroup(): Group

    fun strictGroup(): Boolean = false

    override fun canExecute(user: User?): Boolean {
        if (user == null) {
            return false
        }

        return if (strictGroup()) {
            user.hasStrictGroup(getGroup(), getServer())
        } else {
            user.hasGroup(getGroup(), getServer())
        }
    }

    override fun getErrorMessage() = ComponentBuilder(
        "§cÉ necessário o grupo ${getGroup().displayName} ou superior para executar este comando."
    ).create()

}