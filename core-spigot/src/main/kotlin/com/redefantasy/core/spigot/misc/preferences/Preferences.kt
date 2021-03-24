package com.redefantasy.core.spigot.misc.preferences

import com.redefantasy.core.shared.misc.preferences.PreferenceIcon
import com.redefantasy.core.spigot.misc.utils.ItemBuilder
import org.bukkit.Material

/**
 * @author Gutyerrez
 */
inline fun PreferenceIcon.toItemStack() = ItemBuilder(
        Material.valueOf(this.material.name)
    )
    .name(this.displayName)
    .lore(this.lore)
    .build()