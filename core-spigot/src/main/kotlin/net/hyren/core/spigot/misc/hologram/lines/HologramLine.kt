package net.hyren.core.spigot.misc.hologram.lines

import net.hyren.core.spigot.misc.hologram.entity.HologramArmorStand
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.entity.ArmorStand
import org.bukkit.event.entity.CreatureSpawnEvent

/**
 * @author Gutyerrez
 */
class HologramLine(
    private var text: String
) {

    private var armorStand: ArmorStand? = null

    fun update(text: String) {
        this.text = text

        this.update()
    }

    fun update() {
        if (this.armorStand?.customName != this.text) {
            this.armorStand?.isCustomNameVisible = this.text.isNotEmpty()
            this.armorStand?.customName = this.text
        }
    }

    fun isSpawned() = this.armorStand !== null && !this.armorStand!!.isDead

    fun spawn(location: Location) {
        val worldServer = (location.world as CraftWorld).handle
        val chunkProviderServer = worldServer.chunkProviderServer

        chunkProviderServer.loadChunk(location.blockX, location.blockZ)

        val hologramArmorStand = HologramArmorStand(worldServer, location.x, location.y, location.z)

        hologramArmorStand.setPosition(location.x, location.y, location.z)

        worldServer.addEntity(hologramArmorStand, CreatureSpawnEvent.SpawnReason.CUSTOM)

        this.armorStand = hologramArmorStand.bukkitEntity as ArmorStand

        this.update()
    }

    fun destroy() = this.armorStand?.remove()

    fun teleport(location: Location) {
        this.armorStand?.teleport(location)
    }

}