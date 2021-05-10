package net.hyren.core.spigot.misc.hologram.lines

import net.hyren.core.spigot.CoreSpigotPlugin
import net.hyren.core.spigot.misc.player.sendPacket
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld

/**
 * @author Gutyerrez
 */
class HologramLine(
    private var text: String
) {

    private lateinit var entityArmorStand: EntityArmorStand

    fun update(text: String) {
        this.text = text

        this.update()
    }

    fun update() {
        if (isSpawned()) {
            val dataWatcher = entityArmorStand.dataWatcher

            dataWatcher.watch(2, this.text)
            dataWatcher.watch(3, 1.toByte())

            val packet = PacketPlayOutEntityMetadata(entityArmorStand.id, dataWatcher, true)

            Bukkit.getOnlinePlayers().forEach { it.sendPacket(packet) }

            println("Atualizar linhas")

            println(entityArmorStand.locX)
            println(entityArmorStand.locY)
            println(entityArmorStand.locZ)
        }
    }

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

        val packet = PacketPlayOutSpawnEntityLiving(entityArmorStand)

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CoreSpigotPlugin.instance, {
            Bukkit.getOnlinePlayers().forEach { it.sendPacket(packet) }
        }, 20, 20)

        this.update()
    }

    fun destroy() {
        val packet = PacketPlayOutEntityDestroy(entityArmorStand.id)

        Bukkit.getOnlinePlayers().forEach { player -> player.sendPacket(packet) }
    }

    fun teleport(location: Location) {
        val packet = PacketPlayOutEntityTeleport(
            entityArmorStand.id,
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