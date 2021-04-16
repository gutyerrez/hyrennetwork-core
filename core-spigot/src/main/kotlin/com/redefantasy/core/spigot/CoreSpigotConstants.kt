package com.redefantasy.core.spigot

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.servers.ServerType
import com.redefantasy.core.spigot.misc.utils.ProtocolHandler
import com.redefantasy.core.spigot.world.location.parser.BukkitLocationParser

/**
 * @author Gutyerrez
 */
object CoreSpigotConstants {

    val BUKKIT_LOCATION_PARSER = BukkitLocationParser()

    val PROTOCOL_HANDLER = if (CoreProvider.application.server?.serverType == ServerType.FACTIONS) {
        null
    } else ProtocolHandler()

}