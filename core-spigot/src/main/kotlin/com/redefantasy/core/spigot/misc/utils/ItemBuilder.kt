package com.redefantasy.core.spigot.misc.utils

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
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.material.MaterialData
import org.bukkit.potion.PotionEffect
import java.util.function.Consumer
import java.util.function.Function

/**
 * @author Gutyerrez
 */
class ItemBuilder(
    private val itemStack: ItemStack
) {

    lateinit var itemMeta: ItemMeta

    constructor(
        material: Material
    ) : this(ItemStack(material))

    fun amount(amount: Int): ItemBuilder {
        itemStack.amount = amount

        return this
    }

    fun name(name: String): ItemBuilder {
        this.itemMeta.displayName = ChatColor.translateAlternateColorCodes(
            '&',
            name
        )

        return this
    }

    fun lore(lore: Array<String>): ItemBuilder {
        return this.lore(lore, false)
    }

    fun lore(lore: Array<String>, override: Boolean): ItemBuilder {
        val lines = lore.map { ChatColor.translateAlternateColorCodes('&', it) }.toMutableList()

        if (!override) {
            val oldLines = this.itemMeta.lore

            if (oldLines !== null && oldLines.isNotEmpty()) {
                lines.addAll(0, oldLines)
            }
        }

        this.itemMeta.lore = lines

        return this
    }

    fun durability(durability: Int): ItemBuilder {
        this.itemStack.durability = durability.toShort()

        return this
    }

    fun data(data: Int): ItemBuilder {
        this.itemStack.data = MaterialData(
            this.itemStack.type,
            data.toByte()
        )

        return this
    }

    fun patterns(patterns: Array<Pattern>): ItemBuilder {
        if (this.itemStack.type === Material.BANNER) {
            (this.itemMeta as BannerMeta).patterns = patterns.toList()
        }

        return this
    }

    fun glowing(glowing: Boolean): ItemBuilder {
        if (this.itemStack.type === Material.GOLDEN_APPLE) {
            this.durability(if (glowing) 1 else 0)
        }

        if (this.itemStack.enchantments.isEmpty()) {
            if (glowing) {
                createNBT { it.set("ench", NBTTagList()) }
            } else removeNBT("ench")
        }

        return this
    }

    fun clearFlags(flags: Array<ItemFlag>): ItemBuilder {
        this.itemMeta.removeItemFlags(*flags)

        return this
    }

    fun flags(flags: Array<ItemFlag>): ItemBuilder {
        this.itemMeta.addItemFlags(*flags)

        return this
    }

    fun persistent(boolean: Boolean): ItemBuilder {
        this.itemMeta.spigot().isPersistent = boolean

        return this
    }

    fun enchant(enchantment: Enchantment, level: Int): ItemBuilder {
        this.itemStack.addUnsafeEnchantment(
            enchantment,
            level
        )

        return this
    }

    fun enchant(enchantment: Enchantment): ItemBuilder {
        this.itemStack.addUnsafeEnchantment(enchantment, 1)

        return this
    }

    fun enchantments(enchantments: Array<Enchantment>, level: Int): ItemBuilder {
        enchantments.forEach { this.enchant(it, level) }

        return this
    }

    fun enchantments(enchantments: Array<Enchantment>): ItemBuilder {
        enchantments.forEach { this.enchant(it) }

        return this
    }

    fun clearEnchantment(enchantment: Enchantment): ItemBuilder {
        this.itemStack.removeEnchantment(enchantment)

        return this
    }

    fun clearEnchantments(enchantments: Array<Enchantment>): ItemBuilder {
        enchantments.forEach { this.clearEnchantment(it) }

        return this
    }

    fun effect(potionEffect: PotionEffect, overwrite: Boolean): ItemBuilder {
        if (this.itemMeta is PotionMeta) {
            (this.itemMeta as PotionMeta).addCustomEffect(potionEffect, overwrite)
        }

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
                this.itemStack.type
        )) {
            (this.itemMeta as LeatherArmorMeta).color = color
        }

        return this
    }

    fun color(baseColor: DyeColor): ItemBuilder {
        if (this.itemStack.type === Material.BANNER) {
            (this.itemMeta as BannerMeta).baseColor = baseColor
        }

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
                this.itemStack.type
        )) {
            (this.itemMeta as LeatherArmorMeta).color = null
        }

        if (this.itemStack.type === Material.BANNER) {
            (this.itemMeta as BannerMeta).baseColor = null
        }

        return this
    }

    fun skullOwner(owner: String): ItemBuilder {
        TODO("Não implementado")
    }

    fun skull(player: Player): ItemBuilder {
        TODO("Não implementado")
    }

    fun skull(skin: Skin): ItemBuilder {
        TODO("Não implementado")
    }

    fun skullURL(id: String): ItemBuilder {
        TODO("Não implementado")
    }

    fun createNBT(consumer: Consumer<NBTTagCompound>): NBTTagCompound {
        val nmsCopy = CraftItemStack.asNMSCopy(this.itemStack)

        val compound = if (nmsCopy.hasTag()) nmsCopy.tag else NBTTagCompound()

        consumer.accept(compound)

        nmsCopy.tag = compound

        this.itemMeta = CraftItemStack.asBukkitCopy(nmsCopy).itemMeta

        return compound
    }

    fun <T> createNBT(function: Function<NBTTagCompound, T>): T {
        val nmsCopy = CraftItemStack.asNMSCopy(this.itemStack)

        val compound = if (nmsCopy.hasTag()) nmsCopy.tag else NBTTagCompound()

        return function.apply(compound)
    }

    private fun removeNBT(key: String) {
        val nmsCopy = CraftItemStack.asNMSCopy(this.itemStack)

        val compound = if (nmsCopy.hasTag()) nmsCopy.tag else NBTTagCompound()

        return compound.remove(key)
    }

    private fun ItemStack.hasNBT(key: String): Boolean {
        val nmsCopy = CraftItemStack.asNMSCopy(this)

        val compound = if (nmsCopy.hasTag()) nmsCopy.tag else NBTTagCompound()

        return compound.hasKey(key)
    }

    fun NBT(key: String, value: NBTBase): ItemBuilder {
        this.createNBT { it.set(key, value) }

        return this
    }

    fun NBT(key: String, value: Int): ItemBuilder {
        this.createNBT { it.setInt(key, value) }

        return this
    }

    fun NBT(key: String, value: Boolean): ItemBuilder {
        this.createNBT { it.setBoolean(key, value) }

        return this
    }

    fun NBT(key: String, value: Long): ItemBuilder {
        this.createNBT { it.setLong(key, value) }

        return this
    }

    fun NBT(key: String, value: String): ItemBuilder {
        this.createNBT { it.setString(key, value) }

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

    fun build(): ItemStack {
        this.itemStack.itemMeta = this.itemMeta

        return this.itemStack
    }

}