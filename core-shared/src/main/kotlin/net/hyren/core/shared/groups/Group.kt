package net.hyren.core.shared.groups

import net.hyren.core.shared.misc.utils.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import kotlin.properties.Delegates

/**
 * @author SrGutyerrez
 **/
enum class Group {

    MASTER,
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

    lateinit var displayName: String
    lateinit var prefix: Array<BaseComponent>

    var suffix: Array<BaseComponent>? = null

    var priority by Delegates.notNull<Int>()
    var discordRoleId by Delegates.notNull<Long>()

    companion object {

        @Deprecated(
            "Use manager",
            ReplaceWith(
                "Group.MANAGER"
            )
        )
        val DIRECTOR = MANAGER

    }

    @Deprecated(
        "Use prefix only",
        ReplaceWith(
            "prefix"
        )
    )
    fun getColoredPrefix() = prefix

    fun getStripedPrefix() = ChatColor.stripColor(
        BaseComponent.toLegacyText(*prefix)
    )

    fun getFancyDisplayName() = ChatColor.getLastColors(
        BaseComponent.toLegacyText(*prefix)
    ) + displayName

}