package net.hyren.core.spigot.misc.server.configuration.data

import kotlinx.serialization.Serializable
import net.hyren.core.spigot.misc.json.ItemStackSerializer
import net.hyren.core.spigot.misc.json.ServerSettingsSerializer
import net.hyren.core.spigot.misc.server.configuration.settings.ServerSettings
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
@Serializable
data class ServerConfiguration(
    @Serializable(ServerSettingsSerializer::class)
    val settings: ServerSettings,
    @Serializable(ItemStackSerializer::class)
    val icon: ItemStack? = null
)
