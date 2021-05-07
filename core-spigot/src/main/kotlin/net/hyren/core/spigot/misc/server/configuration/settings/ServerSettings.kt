package net.hyren.core.spigot.misc.server.configuration.settings

import com.fasterxml.jackson.annotation.JsonProperty
import net.hyren.core.shared.world.location.SerializedLocation

/**
 * @author Gutyerrez
 */
data class ServerSettings(
	@JsonProperty("max_players")
	val maxPlayers: Int,
	@JsonProperty("view_distance")
	val viewDistance: Int,
	@JsonProperty("spawn_location")
	val spawnLocation: SerializedLocation,
	@JsonProperty("npc_location")
	val npcLocation: SerializedLocation
)
