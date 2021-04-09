package com.redefantasy.core.spigot.misc.player

import net.minecraft.server.v1_8_R3.Packet
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

/**
 * @author Gutyerrez
 */
fun Player.sendPacket(packet: Packet<*>) {
    val craftPlayer = this as CraftPlayer
    val handle = craftPlayer.handle
    val playerConnection = handle.playerConnection

    playerConnection.sendPacket(packet)
}

fun Player.openInventory(
    inventory: Inventory,
    backInventory: Inventory
) {

}