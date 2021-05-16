package net.hyren.core.spigot.misc.server.configuration.settings

import net.hyren.core.shared.world.location.SerializedLocation

/**
 * @author Gutyerrez
 */
data class 	ServerSettings(
	val maxPlayers: Int,
	val viewDistance: Int,
	val spawnLocation: SerializedLocation,
	val npcLocation: SerializedLocation
)
