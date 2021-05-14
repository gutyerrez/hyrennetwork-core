package net.hyren.core.spigot.misc.server.configuration.data

import kotlinx.serialization.Serializable
import net.hyren.core.spigot.misc.jackson.ItemStackSerializer
import net.hyren.core.spigot.misc.server.configuration.settings.ServerSettings
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
@Serializable
data class ServerConfiguration(
    val settings: ServerSettings,
    @Serializable(ItemStackSerializer::class)
    val icon: ItemStack? = null
)
