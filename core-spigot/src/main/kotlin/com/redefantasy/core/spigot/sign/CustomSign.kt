package com.redefantasy.core.spigot.sign

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.server.v1_8_R3.ChatComponentText
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.NBTTagCompound
import net.minecraft.server.v1_8_R3.TileEntitySign

/**
 * @author Gutyerrez
 */
class CustomSign : TileEntitySign() {

	val nbtModifier = NBTTagCompound()

	fun lines(vararg lines: String): CustomSign {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.lines[index] = ChatComponentText(it)
		}

		this.updateNBTModifier()

		return this
	}

	fun lines(lines: Array<BaseComponent>): CustomSign {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.lines[index] = ChatComponentText(
				ComponentSerializer.toString(it)
			)
		}

		this.updateNBTModifier()

		return this
	}

	fun lines(vararg lines: TextComponent): CustomSign {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.lines[index] = ChatComponentText(
				ComponentSerializer.toString(it)
			)
		}

		this.updateNBTModifier()

		return this
	}

	private fun updateNBTModifier() {
		this.lines.forEachIndexed { index, iChatBaseComponent ->
			nbtModifier.setString(
				"Text" + (index + 1),
				IChatBaseComponent.ChatSerializer.a(iChatBaseComponent)
			)
		}
	}

}