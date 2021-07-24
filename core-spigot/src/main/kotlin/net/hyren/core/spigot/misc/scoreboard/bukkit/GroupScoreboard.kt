package net.hyren.core.spigot.misc.scoreboard.bukkit

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.misc.utils.SequencePrefix
import net.hyren.core.shared.users.data.User
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

/**
 * @author Gutyerrez
 */
open class GroupScoreboard : BaseScoreboard {

    constructor() : super()

    constructor(player: Player): super(player)

    fun registerTeams() {
        Group.values().forEach {
            fetchOrCreateTeam(it)
        }
    }

    fun registerUser(user: User) {
        val group = user.getHighestGroup()

        val previousTeam = scoreboard.getEntryTeam(user.name)

        val newTeam = fetchOrCreateTeam(group)

        if (newTeam != previousTeam) {
            if (previousTeam !== null) previousTeam.removeEntry(user.name)

            newTeam.addEntry(user.name)
        }
    }

    private fun fetchOrCreateTeam(group: Group): Team {
        val teamName = getName(group)

        var team: Team? = scoreboard.getTeam(teamName)

        return if (team === null) {
            team = scoreboard.registerNewTeam(teamName)

            team.prefix = BaseComponent.toLegacyText(*group.prefix)

            team
        } else team
    }

    private fun getName(group: Group): String {
        val index = -group.priority + Group.MASTER.priority + 1

        val sequencePrefix = SequencePrefix()

        var prefix = "zzz"

        for (i in 0 until index) {
            prefix = sequencePrefix.next()
        }

        return "__$prefix"
    }

}