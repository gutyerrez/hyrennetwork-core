package net.hyren.core.spigot.misc.hologram.lines

import net.hyren.core.spigot.misc.player.sendPacket
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage

/**
 * @author Gutyerrez
 */
class HologramLine(
    private var text: String
) {

    private lateinit var livingEntity: EntityArmorStand

    fun update(text: String) {
        this.text = text

        this.update()
    }

    fun update() {
        if (isSpawned()) {
            val dataWatcher = livingEntity.dataWatcher

            dataWatcher.watch(2, CraftChatMessage.fromString(this.text))
            dataWatcher.watch(3, 1.toByte())

            val packet = PacketPlayOutEntityMetadata(livingEntity.id, dataWatcher, true)

            Bukkit.getOnlinePlayers().forEach { it.sendPacket(packet) }
        }
    }

    fun isSpawned() = this::livingEntity.isInitialized && !livingEntity.dead

    fun spawn(location: Location) {
        val worldServer = (location.world as CraftWorld).handle

        this.livingEntity = EntityArmorStand(worldServer, location.x, location.y, location.z)

        this.livingEntity.isSmall = true
        this.livingEntity.customNameVisible = true
        this.livingEntity.isInvisible = true
        this.livingEntity.noclip = true

        this.livingEntity.setArms(false)
        this.livingEntity.setGravity(true)
        this.livingEntity.setBasePlate(false)

        this.livingEntity.n(true)

        val packet = PacketPlayOutSpawnEntityLiving(livingEntity)

        Bukkit.getOnlinePlayers().forEach { it.sendPacket(packet) }

        this.update()
    }

    fun destroy() {
        val packet = PacketPlayOutEntityDestroy(livingEntity.id)

        Bukkit.getOnlinePlayers().forEach { player -> player.sendPacket(packet) }
    }

    fun teleport(location: Location) {
        val packet = PacketPlayOutEntityTeleport(
            livingEntity.id,
            MathHelper.floor(location.x * 32.0),
            MathHelper.floor(location.y * 32.0),
            MathHelper.floor(location.z * 32.0),
            (location.yaw * 256.0f / 360.0f).toInt().toByte(),
            (location.yaw * 256.0f / 360.0f).toInt().toByte(),
            false
        )

        Bukkit.getOnlinePlayers().forEach { player -> player.sendPacket(packet) }
    }
}