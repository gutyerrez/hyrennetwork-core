package com.redefantasy.core.spigot

import com.redefantasy.core.spigot.misc.utils.ProtocolHandler
import com.redefantasy.core.spigot.world.location.parser.BukkitLocationParser
import org.bukkit.Bukkit

/**
 * @author Gutyerrez
 */
object CoreSpigotConstants {

    val BUKKIT_LOCATION_PARSER = BukkitLocationParser()

    val PROTOCOL_HANDLER = if (Bukkit.getPluginManager().isPluginEnabled("Protocollib")) {
        null
    } else ProtocolHandler()

}