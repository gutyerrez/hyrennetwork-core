package net.hyren.core.shared.misc.chat

import net.md_5.bungee.api.chat.BaseComponent

/**
 * @author Gutyerrez
 */
operator fun net.md_5.bungee.api.ChatColor.plus(other: String) = this.toString() + other

operator fun Array<net.md_5.bungee.api.chat.BaseComponent>.plus(
    other: String
) = BaseComponent.toLegacyText(*this) + other

operator fun net.hyren.core.shared.misc.utils.ChatColor.plus(other: String) = this.toString() + other
