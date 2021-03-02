package com.redefantasy.core.bungee.command.defaults.player.ignore

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.player.ignore.subcommands.IgnoreAddCommand
import com.redefantasy.core.bungee.command.defaults.player.ignore.subcommands.IgnoreListCommand
import com.redefantasy.core.bungee.command.defaults.player.ignore.subcommands.IgnoreRemoveCommand
import com.redefantasy.core.shared.commands.restriction.CommandRestriction

/**
 * @author Gutyerrez
 */
class IgnoreCommand : CustomCommand("ignorar") {

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getSubCommands() = listOf(
        IgnoreAddCommand(),
        IgnoreRemoveCommand(),
        IgnoreListCommand()
    )

}