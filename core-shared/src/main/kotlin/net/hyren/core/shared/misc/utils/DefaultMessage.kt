package net.hyren.core.shared.misc.utils

import net.md_5.bungee.api.chat.TextComponent

/**
 * @author SrGutyerrez
 **/
object DefaultMessage {

    val USER_NOT_FOUND = TextComponent("§cUsuário não existe.")
    val USER_NOT_ONLINE = TextComponent("§cO usuário não está online.")
    val NO_PERMISSION_STRICT = TextComponent("§cÉ necessário o grupo %s para fazer isso.")
    val NO_PERMISSION = TextComponent("§cÉ necessário o grupo %s ou superior para fazer isso.")
    val COMBAT_TELEPORT_ERROR = TextComponent("§cVocê não pode se teletranspotar em combate.")
    val COMBAT_COMMAND_ERROR = TextComponent("§cVocê não pode executar comandos em combate.")

}