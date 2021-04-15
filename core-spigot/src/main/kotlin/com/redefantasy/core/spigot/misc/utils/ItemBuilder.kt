package com.redefantasy.core.spigot.misc.utils

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import com.redefantasy.core.shared.misc.skin.Skin
import com.redefantasy.core.shared.misc.utils.ChatColor
import net.minecraft.server.v1_8_R3.NBTBase
import net.minecraft.server.v1_8_R3.NBTTagCompound
import net.minecraft.server.v1_8_R3.NBTTagList
import org.apache.commons.lang3.ArrayUtils
import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.material.MaterialData
import org.bukkit.potion.PotionEffect
import java.util.*
import java.util.function.Consumer
import java.util.function.Function

/**
 * @author Gutyerrez
 */
class ItemBuilder(
	private var itemStack: ItemStack
) {

	private var itemMeta = itemStack.itemMeta

	constructor(
		material: Material
	) : this(ItemStack(material))

	fun amount(amount: Int) = apply {
		itemStack.amount = amount
	}

	fun name(name: String) = apply {
		itemMeta.displayName = ChatColor.translateAlternateColorCodes(
			'&',
			name
		)

		itemStack.itemMeta = itemMeta
	}

	fun lore(lore: Array<String>) = apply {
		return lore(lore, false)
	}

	fun lore(lore: Array<String>, override: Boolean) = apply {
		val lines = lore.map { ChatColor.translateAlternateColorCodes('&', it) }.toMutableList()

		if (!override) {
			val oldLines = itemMeta.lore

			if (oldLines !== null && oldLines.isNotEmpty()) {
				lines.addAll(0, oldLines)
			}
		}

		itemMeta.lore = lines
		itemStack.itemMeta = itemMeta
	}

	fun durability(durability: Int) = apply {
		itemStack.durability = durability.toShort()

		itemStack.itemMeta = itemMeta
	}

	fun data(data: Int) = apply {
		itemStack.data = MaterialData(
			itemStack.type,
			data.toByte()
		)

		itemStack.itemMeta = itemMeta
	}

	fun patterns(patterns: Array<Pattern>) = apply {
		if (itemStack.type === Material.BANNER) {
			(itemMeta as BannerMeta).patterns = patterns.toList()
		}

		itemStack.itemMeta = itemMeta
	}

	fun glowing(glowing: Boolean = true) = apply {
		if (itemStack.type === Material.GOLDEN_APPLE) {
			durability(if (glowing) 1 else 0)
		}

		if (itemStack.enchantments.isEmpty() && glowing) {
			createNBT {
				it.set("ench", NBTTagList())
			}
		} else if (!glowing) removeNBT("ench")
	}

	fun clearFlags(flags: Array<ItemFlag>) = apply {
		itemMeta.removeItemFlags(*flags)
		itemStack.itemMeta = itemMeta
	}

	fun flags(flags: Array<ItemFlag>) = apply {
		itemMeta.addItemFlags(*flags)

		itemStack.itemMeta = itemMeta
	}

	fun persistent(boolean: Boolean) = apply {
		itemMeta.spigot().isPersistent = boolean

		itemStack.itemMeta = itemMeta
	}

	fun enchant(enchantment: Enchantment, level: Int) = apply {
		itemStack.addUnsafeEnchantment(
			enchantment,
			level
		)
	}

	fun enchant(enchantment: Enchantment) = apply {
		itemStack.addUnsafeEnchantment(enchantment, 1)
	}

	fun enchantments(enchantments: Array<Enchantment>, level: Int) = apply {
		enchantments.forEach { enchant(it, level) }
	}

	fun enchantments(enchantments: Array<Enchantment>) = apply {
		enchantments.forEach { enchant(it) }
	}

	fun clearEnchantment(enchantment: Enchantment) = apply {
		itemStack.removeEnchantment(enchantment)
	}

	fun clearEnchantments(enchantments: Array<Enchantment>) = apply {
		enchantments.forEach { clearEnchantment(it) }
	}

	fun effect(potionEffect: PotionEffect, overwrite: Boolean) = apply {
		if (itemMeta is PotionMeta) {
			(itemMeta as PotionMeta).addCustomEffect(potionEffect, overwrite)
		}

		itemStack.itemMeta = itemMeta
	}

	fun color(color: Color) = apply {
		if (ArrayUtils.contains(
				arrayOf(
					Material.LEATHER_HELMET,
					Material.LEATHER_CHESTPLATE,
					Material.LEATHER_LEGGINGS,
					Material.LEATHER_BOOTS,
				),
				itemStack.type
			)
		) {
			(itemMeta as LeatherArmorMeta).color = color
		}

		itemStack.itemMeta = itemMeta
	}

	fun color(baseColor: DyeColor) = apply {
		if (itemStack.type === Material.BANNER) {
			(itemMeta as BannerMeta).baseColor = baseColor
		}

		itemStack.itemMeta = itemMeta
	}

	fun clearColor() = apply {
		if (ArrayUtils.contains(
				arrayOf(
					Material.LEATHER_HELMET,
					Material.LEATHER_CHESTPLATE,
					Material.LEATHER_LEGGINGS,
					Material.LEATHER_BOOTS,
				),
				itemStack.type
			)
		) {
			(itemMeta as LeatherArmorMeta).color = null
		}

		if (itemStack.type === Material.BANNER) {
			(itemMeta as BannerMeta).baseColor = null
		}

		itemStack.itemMeta = itemMeta
	}

	fun skullOwner(owner: String) = apply {
		if (itemStack.type == Material.SKULL_ITEM && itemStack.durability == 3.toShort()) {
			val skullMeta = itemMeta as SkullMeta

			skullMeta.owner = owner

			itemStack.itemMeta = skullMeta
		}
	}

	fun skull(player: Player) = apply {
		if (itemStack.type == Material.SKULL_ITEM && itemStack.durability == 3.toShort()) {
			val skullMeta = itemMeta as SkullMeta

			skullMeta.owner = "CustomSkull"

			val playerProfile = (player as CraftPlayer).profile
			val gameProfile = GameProfile(UUID.randomUUID(), null)

			gameProfile.properties.putAll(
				"textures",
				playerProfile.properties["textures"]
			)

			val fieldProfile = skullMeta::class.java.getDeclaredField("profile")

			fieldProfile.isAccessible = true

			fieldProfile.set(skullMeta, gameProfile)

			itemStack.itemMeta = skullMeta
		}
	}

	fun skull(skin: Skin) = apply {
		if (itemStack.type == Material.SKULL_ITEM && itemStack.durability == 3.toShort()) {
			val skullMeta = itemMeta as SkullMeta

			skullMeta.owner = "CustomSkull"

			val gameProfile = GameProfile(UUID.randomUUID(), null)

			gameProfile.properties.put(
				"textures",
				Property(
					"textures",
					skin.value,
					skin.signature
				)
			)

			val fieldProfile = skullMeta::class.java.getDeclaredField("profile")

			fieldProfile.isAccessible = true

			fieldProfile.set(skullMeta, gameProfile)

			itemStack.itemMeta = skullMeta
		}
	}

	fun skull(texture: String) = apply {
		if (itemStack.type == Material.SKULL_ITEM && itemStack.durability == 3.toShort()) {
			val skullMeta = itemMeta as SkullMeta

			skullMeta.owner = "CustomSkull"

			val gameProfile = GameProfile(UUID.randomUUID(), null)

			gameProfile.properties.put(
				"textures",
				Property(
					"textures",
					texture,
					null
				)
			)

			val fieldProfile = skullMeta::class.java.getDeclaredField("profile")

			fieldProfile.isAccessible = true

			fieldProfile.set(skullMeta, gameProfile)

			itemStack.itemMeta = skullMeta
		}
	}

	fun createNBT(consumer: Consumer<NBTTagCompound>): NBTTagCompound {
		val nmsCopy = CraftItemStack.asNMSCopy(itemStack)

		val compound = if (nmsCopy.hasTag()) nmsCopy.tag else NBTTagCompound()

		consumer.accept(compound)

		nmsCopy.tag = compound

		itemStack = CraftItemStack.asBukkitCopy(nmsCopy)

		return compound
	}

	fun <T> createNBT(function: Function<NBTTagCompound, T>): T {
		val nmsCopy = CraftItemStack.asNMSCopy(itemStack)

		val compound = if (nmsCopy.hasTag()) nmsCopy.tag else NBTTagCompound()

		val t = function.apply(compound)

		nmsCopy.tag = compound

		itemStack = CraftItemStack.asBukkitCopy(nmsCopy)

		return t
	}

	private fun removeNBT(key: String) {
		val nmsCopy = CraftItemStack.asNMSCopy(itemStack)

		val compound = if (nmsCopy.hasTag()) nmsCopy.tag else NBTTagCompound()

		return compound.remove(key)
	}

	private fun ItemStack.hasNBT(key: String): Boolean {
		val nmsCopy = CraftItemStack.asNMSCopy(this)

		val compound = if (nmsCopy.hasTag()) nmsCopy.tag else NBTTagCompound()

		return compound.hasKey(key)
	}

	fun NBT(key: String, value: NBTBase) = apply {
		createNBT { it.set(key, value) }
	}

	fun NBT(key: String, value: Int) = apply {
		createNBT { it.setInt(key, value) }
	}

	fun NBT(key: String, value: Boolean) = apply {
		createNBT { it.setBoolean(key, value) }
	}

	fun NBT(key: String, value: Long) = apply {
		createNBT { it.setLong(key, value) }

		return this
	}

	fun NBT(key: String, value: String) = apply {
		createNBT { it.setString(key, value) }
	}

	fun NBTTagString(key: String): String? {
		TODO("Não implementado")
	}

	fun NBTTagInt(key: String): Int? {
		TODO("Não implementado")
	}

	fun NBTTagLong(key: String): Long? {
		TODO("Não implementado")
	}

	fun NBTTagDouble(key: String): Double? {
		TODO("Não implementado")
	}

	fun NBTTagBoolean(key: String): Boolean? {
		TODO("Não implementado")
	}

	fun NBTTagList(key: String): NBTTagList? {
		TODO("Não implementado")
	}

	fun build(): ItemStack {
		this.itemStack.itemMeta = itemMeta

		return this.itemStack
	}

}