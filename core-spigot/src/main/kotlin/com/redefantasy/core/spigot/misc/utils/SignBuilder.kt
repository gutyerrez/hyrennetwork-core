package com.redefantasy.core.spigot.misc.utils

import com.redefantasy.core.shared.misc.kotlin.sizedArray
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.server.v1_8_R3.ChatComponentText
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.TileEntitySign
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
class SignBuilder {

	private val itemStack = ItemStack(Material.SIGN)
	private val sign = TileEntitySign()

	fun lines(vararg lines: String): SignBuilder {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		val _lines = sizedArray<IChatBaseComponent>(4)

		lines.forEachIndexed { index, it ->
			_lines[index] = ChatComponentText(it)
		}

		sign.setLines(_lines)

		return this
	}

	fun lines(lines: Array<BaseComponent>): SignBuilder {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		val _lines = sizedArray<IChatBaseComponent>(4)

		lines.forEachIndexed { index, it ->
			_lines[index] = ChatComponentText(
				ComponentSerializer.toString(it)
			)
		}

		sign.setLines(_lines)

		return this
	}

	fun lines(vararg lines: TextComponent): SignBuilder {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		val _lines = sizedArray<IChatBaseComponent>(4)

		lines.forEachIndexed { index, it ->
			_lines[index] = ChatComponentText(
				ComponentSerializer.toString(it)
			)
		}

		sign.setLines(_lines)

		return this
	}

	fun build() = sign

	private fun TileEntitySign.setLines(lines: Array<IChatBaseComponent>) {
		val field = this::class.java.getDeclaredField("lines")

		field.isAccessible = true

		field.set(this, lines)
	}

}