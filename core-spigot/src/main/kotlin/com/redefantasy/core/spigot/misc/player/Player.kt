package com.redefantasy.core.spigot.misc.player

import io.netty.buffer.Unpooled
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
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
    if (book.type != Material.WRITTEN_BOOK)
        throw IllegalArgumentException("The item stack provided is not a book!")

    this.closeInventory()

    val oldItem = this.itemInHand

    this.itemInHand = book

    player.sendPacket(
        PacketPlayOutCustomPayload("MC|BOpen", PacketDataSerializer(Unpooled.buffer()))
    )

    this.itemInHand = oldItem
}

fun Player.openSignEditor(sign: TileEntitySign) {
    player.sendPacket(
        PacketPlayOutOpenSignEditor(sign.position)
    )
}