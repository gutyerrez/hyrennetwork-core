package com.redefantasy.core.spigot.misc.hologram.entity

import net.minecraft.server.v1_8_R3.DamageSource
import net.minecraft.server.v1_8_R3.EntityArmorStand
import net.minecraft.server.v1_8_R3.ItemStack
import net.minecraft.server.v1_8_R3.World

/**
 * @author Gutyerrez
 */
class HologramArmorStand(
    world: World
) : EntityArmorStand(world) {

    init {
        this.isInvisible = true
        this.isSmall = true
        this.customNameVisible = true
        this.noclip = true

        this.setArms(false)
        this.setGravity(true)
        this.setBasePlate(false)
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