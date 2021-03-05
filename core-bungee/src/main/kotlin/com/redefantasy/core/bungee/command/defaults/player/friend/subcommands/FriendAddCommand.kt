package com.redefantasy.core.bungee.command.defaults.player.friend.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.bungee.command.defaults.player.friend.FriendCommand
import com.redefantasy.core.bungee.echo.packets.FriendAcceptedPacket
import com.redefantasy.core.bungee.echo.packets.FriendRequestPacket
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.commands.argument.Argument
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.friends.data.FriendUser
import com.redefantasy.core.shared.users.friends.storage.dto.CreateFriendUserDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.joda.time.DateTime

/**
 * @author Gutyerrez
 */
class FriendAddCommand : CustomCommand("adicionar") {

    override fun getDescription() = "Enviar uma solicitação de amizade."

    override fun getArguments() = listOf(
        Argument("usuário")
    )

    override fun getParent() = FriendCommand()

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean? {
        val targetUser = CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])

        if (targetUser === null) {
            commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
            return false
        }

        if (targetUser == user) {
            commandSender.sendMessage(TextComponent("§cVocê não pode ser amigo de si mesmo."))
            return false
        }

        if (targetUser.getFriends().contains(user) && user!!.getFriends().contains(targetUser)) {
            commandSender.sendMessage(TextComponent("§cVocê já é amigo deste usuário."))
            return false
        }

        return when {
            user!!.getFriends().contains(targetUser) -> {
                commandSender.sendMessage(TextComponent("§cVocê já enviou uma solicitação de amizade para este usuário e agora precisa aguardá-lo responder."))
                false
            }
            targetUser.getFriends().contains(user) -> {
                CoreProvider.Repositories.Mongo.USERS_FRIENDS_REPOSITORY.provide().create(
                    CreateFriendUserDTO(
                        FriendUser(
                            user.id,
                            targetUser.id,
                            DateTime.now(
                                CoreConstants.DATE_TIME_ZONE
                            )
                        )
                    )
                )

                val message = ComponentBuilder()
                    .append("\n")
                    .append("§eO usuário ${targetUser.getFancyName()}§e agora é seu amigo!")
                    .append("\n\n")
                    .create()

                val _message = ComponentBuilder()
                    .append("\n")
                    .append("§eO usuário ${user.getFancyName()}§e agora é seu amigo!")
                    .append("\n\n")
                    .create()

                val packet = FriendAcceptedPacket()

                packet.targetUserId = targetUser.id
                packet.message = _message

                CoreProvider.Databases.Redis.ECHO.provide().publishToApplications(
                    packet,
                    CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(
                        ApplicationType.PROXY
                    )
                )

                commandSender.sendMessage(*message)
                false
            }
            else -> {
                CoreProvider.Repositories.Mongo.USERS_FRIENDS_REPOSITORY.provide().create(
                    CreateFriendUserDTO(
                        FriendUser(
                            user.id,
                            targetUser.id,
                            DateTime.now(
                                CoreConstants.DATE_TIME_ZONE
                            )
                        )
                    )
                )

                val message = ComponentBuilder()
                    .append("\n")
                    .append("§aVocê recebeu uma solicitação de amizade de ${user.getFancyName()}§a.")
                    .append("\n")
                    .append("§aClique ")
                    .append("§a§lAQUI")
                    .event(
                        ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/amigo aceitar ${user.name}"
                        )
                    )
                    .append("§a para aceitar e ")
                    .append("§c§lAQUI")
                    .event(
                        ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/amigo negar ${user.name}"
                        )
                    )
                    .append("\n\n")
                    .create()

                val packet = FriendRequestPacket()

                packet.userId = user.id
                packet.targetId = targetUser.id
                packet.message = message

                CoreProvider.Databases.Redis.ECHO.provide().publishToApplications(
                    packet,
                    CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByApplicationType(
                        ApplicationType.PROXY
                    )
                )
                false
            }
        }
    }
}