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

	fun amount(amount: Int): ItemBuilder {
		itemStack.amount = amount

		return this
	}

	fun name(name: String): ItemBuilder {
		itemMeta.displayName = ChatColor.translateAlternateColorCodes(
			'&',
			name
		)
		itemStack.itemMeta = itemMeta

		return this
	}

	fun lore(lore: Array<String>): ItemBuilder {
		return lore(lore, false)
	}

	fun lore(lore: Array<String>, override: Boolean): ItemBuilder {
		val lines = lore.map { ChatColor.translateAlternateColorCodes('&', it) }.toMutableList()

		if (!override) {
			val oldLines = itemMeta.lore

			if (oldLines !== null && oldLines.isNotEmpty()) {
				lines.addAll(0, oldLines)
			}
		}

		itemMeta.lore = lines
		itemStack.itemMeta = itemMeta

		return this
	}

	fun durability(durability: Int): ItemBuilder {
		itemStack.durability = durability.toShort()
		itemStack.itemMeta = itemMeta

		return this
	}

	fun data(data: Int): ItemBuilder {
		itemStack.data = MaterialData(
			itemStack.type,
			data.toByte()
		)
		itemStack.itemMeta = itemMeta

		return this
	}

	fun patterns(patterns: Array<Pattern>): ItemBuilder {
		if (itemStack.type === Material.BANNER) {
			(itemMeta as BannerMeta).patterns = patterns.toList()
		}
		itemStack.itemMeta = itemMeta

		return this
	}

	fun glowing(glowing: Boolean = true): ItemBuilder {
		if (itemStack.type === Material.GOLDEN_APPLE) {
			durability(if (glowing) 1 else 0)
		}

		if (itemStack.enchantments.isEmpty()) {
			println(1)

			if (glowing) {
				println(2)

				createNBT {
					println(8)

					it.set("ench", NBTTagList())
				}
			} else {
				println(3)

				removeNBT("ench")
			}
		} else println(4)

		itemStack.itemMeta = itemMeta
		return this
	}

	fun clearFlags(flags: Array<ItemFlag>): ItemBuilder {
		itemMeta.removeItemFlags(*flags)
		itemStack.itemMeta = itemMeta

		return this
	}

	fun flags(flags: Array<ItemFlag>): ItemBuilder {
		itemMeta.addItemFlags(*flags)
		itemStack.itemMeta = itemMeta

		return this
	}

	fun persistent(boolean: Boolean): ItemBuilder {
		itemMeta.spigot().isPersistent = boolean
		itemStack.itemMeta = itemMeta

		return this
	}

	fun enchant(enchantment: Enchantment, level: Int): ItemBuilder {
		itemStack.addUnsafeEnchantment(
			enchantment,
			level
		)

		return this
	}

	fun enchant(enchantment: Enchantment): ItemBuilder {
		itemStack.addUnsafeEnchantment(enchantment, 1)

		return this
	}

	fun enchantments(enchantments: Array<Enchantment>, level: Int): ItemBuilder {
		enchantments.forEach { enchant(it, level) }

		return this
	}

	fun enchantments(enchantments: Array<Enchantment>): ItemBuilder {
		enchantments.forEach { enchant(it) }

		return this
	}

	fun clearEnchantment(enchantment: Enchantment): ItemBuilder {
		itemStack.removeEnchantment(enchantment)

		return this
	}

	fun clearEnchantments(enchantments: Array<Enchantment>): ItemBuilder {
		enchantments.forEach { clearEnchantment(it) }

		return this
	}

	fun effect(potionEffect: PotionEffect, overwrite: Boolean): ItemBuilder {
		if (itemMeta is PotionMeta) {
			(itemMeta as PotionMeta).addCustomEffect(potionEffect, overwrite)
		}
		itemStack.itemMeta = itemMeta

		return this
	}

	fun color(color: Color): ItemBuilder {
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

		return this
	}

	fun color(baseColor: DyeColor): ItemBuilder {
		if (itemStack.type === Material.BANNER) {
			(itemMeta as BannerMeta).baseColor = baseColor
		}
		itemStack.itemMeta = itemMeta

		return this
	}

	fun clearColor(): ItemBuilder {
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

		return this
	}

	fun skullOwner(owner: String): ItemBuilder {
		if (itemStack.type == Material.SKULL_ITEM && itemStack.durability == 3.toShort()) {
			val skullMeta = itemMeta as SkullMeta

			skullMeta.owner = owner

			itemStack.itemMeta = skullMeta
		}

		return this
	}

	fun skull(player: Player): ItemBuilder {
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

		return this
	}

	fun skull(skin: Skin): ItemBuilder {
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

		return this
	}

	fun skull(texture: String): ItemBuilder {
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

		return this
	}

	fun createNBT(consumer: Consumer<NBTTagCompound>): NBTTagCompound {
		println(5)

		val nmsCopy = CraftItemStack.asNMSCopy(itemStack)

		val compound = if (nmsCopy.hasTag()) nmsCopy.tag else NBTTagCompound()

		println(6)

		println(consumer)

		consumer.accept(compound)

		nmsCopy.tag = compound

		itemMeta = CraftItemStack.asBukkitCopy(nmsCopy).itemMeta

		println(7)

		itemStack.itemMeta = itemMeta

		println(9)
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

	fun NBT(key: String, value: NBTBase): ItemBuilder {
		createNBT { it.set(key, value) }

		return this
	}

	fun NBT(key: String, value: Int): ItemBuilder {
		createNBT { it.setInt(key, value) }

		return this
	}

	fun NBT(key: String, value: Boolean): ItemBuilder {
		createNBT { it.setBoolean(key, value) }

		return this
	}

	fun NBT(key: String, value: Long): ItemBuilder {
		createNBT { it.setLong(key, value) }

		return this
	}

	fun NBT(key: String, value: String): ItemBuilder {
		createNBT { it.setString(key, value) }

		return this
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

	fun build() = itemStack

}