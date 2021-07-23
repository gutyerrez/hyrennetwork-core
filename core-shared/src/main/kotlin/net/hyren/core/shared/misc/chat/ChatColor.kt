package net.hyren.core.shared.misc.chat

/**
 * @author Gutyerrez
 */
operator fun net.md_5.bungee.api.ChatColor.plus(other: String) = this.toString() + other

operator fun net.hyren.core.shared.misc.utils.ChatColor.plus(other: String) = this.toString() + other