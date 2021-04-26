package com.redefantasy.core.spigot.misc.server.configuration.data

import com.redefantasy.core.spigot.misc.server.configuration.settings.ServerSettings
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
data class ServerConfiguration(
	val settings: ServerSettings,
	val icon: ItemStack? = null
)
