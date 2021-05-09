package net.hyren.core.spigot.misc.hologram.entity

import net.minecraft.server.v1_8_R3.DamageSource
import net.minecraft.server.v1_8_R3.EntityArmorStand
import net.minecraft.server.v1_8_R3.ItemStack
import net.minecraft.server.v1_8_R3.WorldServer

/**
 * @author Gutyerrez
 */
class HologramArmorStand(
    worldServer: WorldServer,
    x: Double,
    y: Double,
    z: Double
) : EntityArmorStand(worldServer, x, y, z) {

    init {
        this.isInvisible = true
        this.isSmall = true
        this.customNameVisible = true
        this.noclip = true

        this.setArms(false)
        this.setGravity(false)
        this.setBasePlate(false)
    }

    override fun d(
        slot: Int,
        itemstack: ItemStack
    ) = false

    override fun damageEntity(
        damagesource: DamageSource,
        totalDamage: Float
    ) = false

}