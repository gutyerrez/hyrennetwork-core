package net.hyren.core.spigot.world.location.parser

import net.hyren.core.shared.world.location.LocationParser
import net.hyren.core.shared.world.location.SerializedLocation
import net.hyren.core.spigot.CoreSpigotConstants
import org.bukkit.Bukkit
import org.bukkit.Location

/**
 * @author Gutyerrez
 */
class BukkitLocationParser : LocationParser<Location> {

    override fun apply(serializedLocation: SerializedLocation) = Location(
        Bukkit.getWorld(
            serializedLocation.worldName
        ),
        serializedLocation.x,
        serializedLocation.y,
        serializedLocation.z,
        serializedLocation.yaw,
        serializedLocation.pitch
    )

}

public inline fun SerializedLocation.asBukkitLocation() = CoreSpigotConstants.BUKKIT_LOCATION_PARSER.apply(this)