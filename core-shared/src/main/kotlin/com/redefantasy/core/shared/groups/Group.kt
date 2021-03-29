package com.redefantasy.core.shared.groups

import com.redefantasy.core.shared.misc.utils.ChatColor

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

    fun getColoredPrefix() = "${ChatColor.fromHEX(color ?: "")}$prefix"

    fun getFancyDisplayName() = "${ChatColor.fromHEX(color ?: "")}$displayName"

}