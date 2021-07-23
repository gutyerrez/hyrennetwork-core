package net.hyren.core.shared.misc.chat

/**
 * @author Gutyerrez
 */
operator fun net.md_5.bungee.api.ChatColor.plus(other: String) = toString() + other

operator fun net.md_5.bungee.api.chat.TextComponent.plus(other: String) = toLegacyText() + other

operator fun Array<net.md_5.bungee.api.chat.BaseComponent>.plus(
    other: String
) = net.md_5.bungee.api.chat.BaseComponent.toLegacyText(*this) + other

operator fun net.hyren.core.shared.misc.utils.ChatColor.plus(other: String) = toString() + other
