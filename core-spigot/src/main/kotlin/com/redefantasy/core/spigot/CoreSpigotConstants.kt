package com.redefantasy.core.spigot

import com.redefantasy.core.spigot.misc.utils.TinyProtocol
import com.redefantasy.core.spigot.world.location.parser.BukkitLocationParser

/**
 * @author Gutyerrez
 */
object CoreSpigotConstants {

    val PROTOCOL = TinyProtocol(
        CoreSpigotPlugin.instance
    )
    val BUKKIT_LOCATION_PARSER = BukkitLocationParser()

}