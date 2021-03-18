package com.redefantasy.core.shared.misc.preferences

import com.redefantasy.core.shared.misc.utils.ChatColor

/**
 * @author Gutyerrez
 */
enum class PreferenceState {

    ENABLED, DISABLED;

    fun getColor() = if (this === ENABLED) {
        ChatColor.GREEN
    } else ChatColor.RED

}