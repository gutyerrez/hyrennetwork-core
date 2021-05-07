package net.hyren.core.spigot.misc.server.configuration.data

import net.hyren.core.spigot.misc.server.configuration.settings.ServerSettings
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
data class ServerConfiguration(
    val settings: ServerSettings,
    val icon: ItemStack? = null
)
