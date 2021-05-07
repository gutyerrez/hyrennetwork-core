package net.hyren.core.shared.misc.preferences

import net.hyren.core.shared.misc.utils.ChatColor

/**
 * @author Gutyerrez
 */
enum class PreferenceState {

    ENABLED, DISABLED;

    fun getColor() = if (this === ENABLED) {
        ChatColor.GREEN
    } else ChatColor.RED

}