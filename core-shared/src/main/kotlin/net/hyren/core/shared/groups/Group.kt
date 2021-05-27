package net.hyren.core.shared.groups

import net.hyren.core.shared.misc.utils.ChatColor

/**
 * @author SrGutyerrez
 **/
enum class Group(
        var displayName: String? = null,
        var prefix: String? = null,
        var suffix: String? = null,
        var color: String? = null,
        var priority: Int? = null,
        var discordRoleId: Long? = null
) {

    MASTER,
    DIRECTOR,
    MANAGER,
    ADMINISTRATOR,
    MODERATOR,
    HELPER,
    BUILDER,
    YOUTUBER,
    MVP_PLUS,
    MVP,
    VIP_PLUS,
    VIP,
    DEFAULT;

    init {
        Group.values().forEach {
            println("${it.name} -> ${it.ordinal}")
        }
    }

    fun getColoredPrefix() = "${ChatColor.fromHEX(color ?: "")}$prefix"

    fun getFancyDisplayName() = "${ChatColor.fromHEX(color ?: "")}$displayName"

}