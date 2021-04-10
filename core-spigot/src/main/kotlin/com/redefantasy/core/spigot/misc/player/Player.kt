package com.redefantasy.core.spigot.misc.player

import com.redefantasy.core.spigot.CoreSpigotPlugin
import com.redefantasy.core.spigot.sign.CustomSign
import io.netty.buffer.Unpooled
import net.minecraft.server.v1_8_R3.Packet
import net.minecraft.server.v1_8_R3.PacketDataSerializer
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue

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
) { /* */
}

fun Player.openBook(book: ItemStack) {
	if (book.type != Material.WRITTEN_BOOK)
		throw IllegalArgumentException("The item stack provided is not a book!")

	this.closeInventory()

	val oldItem = this.itemInHand

	this.itemInHand = book

	this.sendPacket(
		PacketPlayOutCustomPayload("MC|BOpen", PacketDataSerializer(Unpooled.buffer()))
	)

	this.itemInHand = oldItem
}

fun Player.openSignEditor(customSign: CustomSign) {
	val blockPosition = customSign.position
	val location = Location(
		world,
		blockPosition.x.toDouble(),
		blockPosition.y.toDouble(),
		blockPosition.z.toDouble()
	)

	this.sendBlockChange(
		location,
		Material.SIGN_POST,
		0.toByte()
	)
	this.sendSignChange(
		location,
		customSign.textLines
	)

	this.sendPacket(
		PacketPlayOutOpenSignEditor(customSign.position)
	)

	this.setMetadata("custom-sign", FixedMetadataValue(
		CoreSpigotPlugin.instance,
		customSign
	))
}
