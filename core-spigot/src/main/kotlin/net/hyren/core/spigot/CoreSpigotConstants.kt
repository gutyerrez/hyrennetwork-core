package net.hyren.core.spigot

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.servers.ServerType
import net.hyren.core.spigot.misc.utils.ProtocolHandler
import net.hyren.core.spigot.world.location.parser.BukkitLocationParser

/**
 * @author Gutyerrez
 */
object CoreSpigotConstants {

    val BUKKIT_LOCATION_PARSER = BukkitLocationParser()

    val PROTOCOL_HANDLER = if (CoreProvider.application.server?.serverType == ServerType.FACTIONS) {
        null
    } else ProtocolHandler()

}