package com.redefantasy.core.spigot.misc.player

import net.minecraft.server.v1_8_R3.Packet
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

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
) { /* */ }

fun Player.openBook(book: ItemStack) {
    if (book.type != Material.BOOK_AND_QUILL)
        throw IllegalArgumentException("The item stack provided is not a book!")

    this.closeInventory()

    val craftPlayer = this as CraftPlayer
    val handle = craftPlayer.handle

    handle.openBook(
        CraftItemStack.asNMSCopy(book)
    )
}