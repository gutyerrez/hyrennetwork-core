package com.redefantasy.core.shared.groups

/**
 * @author SrGutyerrez
 **/
enum class Group(
        var displayName: String? = null,
        var prefix: String? = null,
        var suffix: String? = null,
        var color: String? = null,
        var priority: Int? = null,
        var tabListOrder: Int? = null,
        var discordRoleId: Long? = null
) {

    GAME_MASTER,
    MANAGER,
    ADMINISTRATOR,
    MODERATOR,
    HELPER,
    BUILDER,
    YOUTUBE,
    ULTIMATE,
    PREMIUM,
    DEFAULT;

}