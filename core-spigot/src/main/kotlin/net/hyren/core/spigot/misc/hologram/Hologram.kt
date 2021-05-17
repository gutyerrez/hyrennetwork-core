package net.hyren.core.spigot.misc.hologram

import net.hyren.core.spigot.misc.hologram.lines.HologramLine
import org.bukkit.Location
import java.util.stream.Collectors

/**
 * @author Gutyerrez
 */
class Hologram(
    _lines: List<String>,
    private val hologramPosition: HologramPosition
) {

    private val lines = mutableListOf<HologramLine>()

    init {
        this.lines.addAll(
            _lines.stream().map { HologramLine(it) }.collect(Collectors.toList())
        )
    }

    fun update(
        position: Int,
        text: String
    ) {
        this.lines[position].update(text)
    }

    fun remove(
        position: Int
    ) {
        if (position >= lines.size) return

        this.lines[position].destroy()

        this.lines.removeAt(position)
    }

    fun update() {
        this.lines.forEach { it.update() }
    }

    fun isSpawned() = this.lines.stream().allMatch { it.isSpawned() }

    fun spawn(location: Location) {
        var hologramLocation = location.clone()

        this.lines.forEach {
            hologramLocation = hologramLocation.add(0.0, this.hologramPosition.value, 0.0)

            it.spawn(hologramLocation)
        }
    }

    fun destroy() = this.lines.forEach { it.destroy() }

    fun teleport(location: Location) {
        var hologramLocation = location.clone()

        this.lines.forEach {
            it.teleport(hologramLocation)

            hologramLocation = hologramLocation.add(0.0, this.hologramPosition.value, 0.0)
        }
    }

    enum class HologramPosition(val value: Double) {

        UP(0.23),
        DOWN(-0.29)

    }

}