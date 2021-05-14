package net.hyren.core.spigot.misc.server.configuration.settings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.hyren.core.shared.world.location.SerializedLocation

/**
 * @author Gutyerrez
 */
@Serializable
data class ServerSettings(
	@SerialName("max_players") val maxPlayers: Int,
	@SerialName("view_distance") val viewDistance: Int,
	@SerialName("spawn_location") val spawnLocation: SerializedLocation,
	@SerialName("npc_location") val npcLocation: SerializedLocation
)
