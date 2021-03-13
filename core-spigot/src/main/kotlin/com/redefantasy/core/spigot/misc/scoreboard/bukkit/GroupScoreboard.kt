package com.redefantasy.core.spigot.misc.scoreboard.bukkit

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.utils.SequencePrefix
import com.redefantasy.core.shared.users.data.User
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
            this.fetchOrCreateTeam(it)
        }
    }

    fun registerUser(user: User) {
        val group = user.getHighestGroup()

        val previousTeam = this.scoreboard.getEntryTeam(user.name)

        val newTeam = this.fetchOrCreateTeam(group)

        if (newTeam != previousTeam) {
            if (previousTeam !== null) previousTeam.removeEntry(user.name)

            newTeam.addEntry(user.name)
        }
    }

    private fun fetchOrCreateTeam(group: Group): Team {
        val teamName = this.getName(group)

        var team: Team? = this.scoreboard.getTeam(teamName)

        return if (team === null) {
            team = this.scoreboard.registerNewTeam(teamName)

            team.prefix = group.getColoredPrefix()

            team
        } else team
    }

    private fun getName(group: Group): String {
        val index = -group.priority!! + Group.MASTER.priority!! + 1

        val sequencePrefix = SequencePrefix()

        var prefix = "zzz"

        for (i in 0 until index) {
            prefix = sequencePrefix.next()
        }

        return "__$prefix"
    }

}