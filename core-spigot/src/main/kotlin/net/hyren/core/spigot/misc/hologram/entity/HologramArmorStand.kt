package net.hyren.core.spigot.misc.hologram.entity

import net.minecraft.server.v1_8_R3.DamageSource
import net.minecraft.server.v1_8_R3.EntityArmorStand
import net.minecraft.server.v1_8_R3.ItemStack
import net.minecraft.server.v1_8_R3.WorldServer

/**
 * @author Gutyerrez
 */
class HologramArmorStand : EntityArmorStand {

    constructor(worldServer: WorldServer) : super(worldServer) {
        this.isSmall = true
        this.customNameVisible = true
        this.isInvisible = true
        this.noclip = true

        setArms(false)
        setGravity(true)
        setBasePlate(false)

        this.n(true)
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