package com.redefantasy.core.bungee.command.defaults.player.friend.subcommands

import com.redefantasy.core.bungee.command.CustomCommand
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.misc.utils.DefaultMessage
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.friends.storage.dto.DeleteFriendUserDTO
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent

/**
 * @author Gutyerrez
 */
class FriendDeleteCommand : CustomCommand("excluir") {

    override fun getDescription() = "Desfazer uma amizade."

    override fun onCommand(
        commandSender: CommandSender,
        user: User?,
        args: Array<out String>
    ): Boolean {
        return when (args.size) {
            1 -> {
                if (user!!.getFriends().isEmpty()) {
                    commandSender.sendMessage(TextComponent("§cVocê ainda não possui nenhum amigo."))
                    return false
                }

                if (args[0] == "todos") {
                    commandSender.sendMessage(
                        *ComponentBuilder()
                            .append("\n")
                            .append("§eVocê está prestes a excluir todos os seus amigos.")
                            .append("\n")
                            .append("§eClique ")
                            .append("§naqui")
                            .event(
                                ClickEvent(
                                    ClickEvent.Action.RUN_COMMAND,
                                    "/amigo excluir todos confirmar"
                                )
                            )
                            .append("§e caso você tenha certeza disso.")
                            .append("\n\n")
                            .create()
                    )
                    return true
                }

                val targetUser = CoreProvider.Cache.Local.USERS.provide().fetchByName(args[0])

                if (targetUser === null) {
                    commandSender.sendMessage(DefaultMessage.USER_NOT_FOUND)
                    return false
                }

                if (targetUser == user) {
                    commandSender.sendMessage(TextComponent("§cVocê não pode fazer isso com você mesmo."))
                    return false
                }

                if (!targetUser.isAFriendOf(user)) {
                    commandSender.sendMessage(TextComponent("§cJá não existe uma amizade entre você e este usuário."))
                    return false
                }

                CoreProvider.Repositories.Mongo.USERS_FRIENDS_REPOSITORY.provide().delete(
                    DeleteFriendUserDTO(
                        targetUser.id,
                        user.id
                    )
                )

                CoreProvider.Repositories.Mongo.USERS_FRIENDS_REPOSITORY.provide().delete(
                    DeleteFriendUserDTO(
                        user.id,
                        targetUser.id
                    )
                )

                CoreProvider.Cache.Local.USERS_FRIENDS.provide().invalidate(user.id)

                CoreProvider.Cache.Local.USERS_FRIENDS.provide().invalidate(targetUser.id)

                commandSender.sendMessage(
                    *ComponentBuilder()
                        .append("\n")
                        .append("§cQue pena, agora você não é mais amigo de ${targetUser.getFancyName()}§c.")
                        .append("\n")
                        .append("§cEsperamos que um dia vocês possam se reconciliar.")
                        .append("\n\n")
                        .create()
                )
                true
            }
            2 -> {
                return if (args[0] == "todos" && args[1] == "confirmar") {
                    user!!.getFriends().forEach {
                        CoreProvider.Repositories.Mongo.USERS_FRIENDS_REPOSITORY.provide().delete(
                            DeleteFriendUserDTO(
                                user.id,
                                it.id
                            )
                        )
                        CoreProvider.Repositories.Mongo.USERS_FRIENDS_REPOSITORY.provide().delete(
                            DeleteFriendUserDTO(
                                it.id,
                                user.id
                            )
                        )
                    }

                    commandSender.sendMessage(
                        *ComponentBuilder()
                            .append("\n")
                            .append("§cQue pena agora você não tem nenhum amigo.")
                            .append("\n")
                            .append("§cEsperamos que um dia voc~e volte a fazer amizades.")
                            .append("\n\n")
                            .create()
                    )

                    true
                } else false
            }
            else -> {
                false
            }
        }
    }

}