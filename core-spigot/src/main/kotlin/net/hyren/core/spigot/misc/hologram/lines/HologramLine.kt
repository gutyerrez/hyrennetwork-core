package net.hyren.core.spigot.misc.hologram.lines

import net.minecraft.server.EntityArmorStand
import net.minecraft.server.WorldServer
import org.bukkit.Location
import org.bukkit.craftbukkit.CraftWorld

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
        _worldServer = (location.world as CraftWorld).handle

        entityArmorStand = EntityArmorStand(_worldServer, location.x, location.y, location.z)

        entityArmorStand.isSmall = true
        entityArmorStand.customNameVisible = true
        entityArmorStand.isInvisible = true
        entityArmorStand.noclip = true

        entityArmorStand.setArms(false)
        entityArmorStand.setGravity(true)
        entityArmorStand.setBasePlate(false)

        entityArmorStand.n(true)

        _worldServer.addEntity(entityArmorStand)

        this.update()
    }

    fun destroy() = if(isSpawned()) this._worldServer.removeEntity(this.entityArmorStand) else Unit

    fun teleport(location: Location) = this.entityArmorStand.teleportTo(location, false)

}