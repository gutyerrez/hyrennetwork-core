package net.hyren.core.spigot.misc.hologram.lines

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

    private lateinit var livingEntity: Entity

    fun update(text: String) {
        this.text = text

        this.update()
    }

    fun update() {
        if (isSpawned()) {
            val packet = PacketPlayOutEntityMetadata(
                livingEntity.id,
                DataWatcher(
                    livingEntity
                ).apply {
                    this.watch(
                        2, IChatBaseComponent.ChatSerializer.a(
                            ChatComponentText(
                                this@HologramLine.text
                            )
                        )
                    )
                }, false
            )

            Bukkit.getOnlinePlayers().forEach { it.sendPacket(packet) }
        }
    }

    fun isSpawned() = this::livingEntity.isInitialized && !livingEntity.dead
    fun spawn(location: Location) {
        val worldServer = (location.world as CraftWorld).handle
        val hologramArmorStand = EntityArmorStand(worldServer)

        this.livingEntity = hologramArmorStand

        hologramArmorStand.setLocation(
            location.x, location.y, location.z, location.yaw, location.pitch
        )
        hologramArmorStand.setPosition(
            location.x, location.y, location.z
        )
        val packet = PacketPlayOutSpawnEntityLiving(hologramArmorStand)

        Bukkit.getOnlinePlayers().forEach { it.sendPacket(packet) }

        this.update()
    }

    fun destroy() {
        val packet = PacketPlayOutEntityDestroy(livingEntity.id)

        Bukkit.getOnlinePlayers().forEach { player -> player.sendPacket(packet) }
    }

    fun teleport(location: Location) {
        val packet = PacketPlayOutEntityTeleport(
            livingEntity.id, MathHelper.floor(location.x * 32.0), MathHelper.floor(location.y * 32.0), MathHelper.floor(location.z * 32.0), (location.yaw * 256.0f / 360.0f).toInt().toByte(), (location.yaw * 256.0f / 360.0f).toInt().toByte(), false
        )

        Bukkit.getOnlinePlayers().forEach { player -> player.sendPacket(packet) }
    }
}