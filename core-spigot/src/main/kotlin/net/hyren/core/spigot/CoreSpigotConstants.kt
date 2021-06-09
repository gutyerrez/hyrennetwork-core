package net.hyren.core.spigot

import net.hyren.core.shared.CoreProvider
import net.hyren.core.spigot.misc.utils.ProtocolHandler
import net.hyren.core.spigot.world.location.parser.BukkitLocationParser

/**
 * @author Gutyerrez
 */
object CoreSpigotConstants {

    val BUKKIT_LOCATION_PARSER = BukkitLocationParser()

    val PROTOCOL_HANDLER = when (CoreProvider.application.server?.name?.value) {
        "FACTIONS_HEADS", "RANK_UP_MACHINES" -> null
        else -> ProtocolHandler()
    }

}
