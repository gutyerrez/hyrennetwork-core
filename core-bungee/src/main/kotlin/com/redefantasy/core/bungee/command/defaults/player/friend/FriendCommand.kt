package com.redefantasy.core.bungee.command.defaults.player.friend

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.player.friend.subcommands.*
import com.redefantasy.core.shared.commands.restriction.CommandRestriction

/**
 * @author Gutyerrez
 */
class FriendCommand : CustomCommand("amigo") {

    override fun getCommandRestriction() = CommandRestriction.GAME

    override fun getSubCommands() = listOf(
        FriendAcceptCommand(),
        FriendAddCommand(),
        FriendCancelCommand(),
        FriendDeleteCommand(),
        FriendClearCommand(),
        FriendListCommand(),
        FriendDenyCommand(),
        FriendRequestsCommand()
    )

}