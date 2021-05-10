package net.hyren.core.spigot.misc.hologram.lines

import net.minecraft.server.v1_8_R3.EntityArmorStand
import net.minecraft.server.v1_8_R3.WorldServer
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld

/**
 * @author Gutyerrez
 */
class HologramLine(
    private var text: String
) {

    private lateinit var entityArmorStand: EntityArmorStand

    private lateinit var _worldServer: WorldServer

    fun update(text: String) {
        this.text = text

        this.update()
    }

    fun update() = if (isSpawned()) entityArmorStand.customName = text else Unit

    fun isSpawned() = this::entityArmorStand.isInitialized && !entityArmorStand.dead

    fun spawn(location: Location) {
        val worldServer = (location.world as CraftWorld).handle

        this.entityArmorStand = EntityArmorStand(worldServer, location.x, location.y, location.z)

        this.entityArmorStand.isSmall = true
        this.entityArmorStand.customNameVisible = true
        this.entityArmorStand.isInvisible = true
        this.entityArmorStand.noclip = true

        this.entityArmorStand.setArms(false)
        this.entityArmorStand.setGravity(true)
        this.entityArmorStand.setBasePlate(false)

        this.entityArmorStand.n(true)

        worldServer.addEntity(entityArmorStand)

        this.update()
    }

    fun destroy() = this._worldServer.removeEntity(this.entityArmorStand)

    fun teleport(location: Location) = this.entityArmorStand.teleportTo(location, false)

}