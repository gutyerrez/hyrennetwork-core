package net.hyren.core.spigot.world.location.parser

import net.hyren.core.shared.world.location.LocationParser
import net.hyren.core.shared.world.location.SerializedLocation
import org.bukkit.Bukkit
import org.bukkit.Location

/**
 * @author Gutyerrez
 */
class BukkitLocationParser : LocationParser<Location> {

    override fun apply(t: SerializedLocation) = Location(
            Bukkit.getWorld(
                t.worldName
            ),
            t.x,
            t.y,
            t.z,
            t.yaw,
            t.pitch
        )

}