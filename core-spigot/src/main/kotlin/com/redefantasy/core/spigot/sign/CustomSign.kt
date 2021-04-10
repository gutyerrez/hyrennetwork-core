package com.redefantasy.core.spigot.sign

import com.redefantasy.core.shared.misc.kotlin.sizedArray
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.minecraft.server.v1_8_R3.BlockPosition
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.NBTTagCompound
import net.minecraft.server.v1_8_R3.TileEntitySign
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class CustomSign(
	blockPosition: BlockPosition? = null
) : TileEntitySign() {

	val nbtTagCompound = NBTTagCompound()
	val textLines = sizedArray<String>(4)

	constructor(player: Player): this(
		player.location
	)

	constructor(location: Location): this(
		BlockPosition(
			location.blockX,
			0,
			location.blockZ
		)
	)

	init {
		this.position = BlockPosition(
			blockPosition?.x ?: 0,
			0,
			blockPosition?.z ?: 0
		)
	}

	fun lines(vararg lines: String): CustomSign {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.textLines[index] = it
		}

		return this
	}

	fun lines(lines: Array<BaseComponent>): CustomSign {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.textLines[index] = it.toPlainText()
		}

		return this
	}

	fun lines(vararg lines: TextComponent): CustomSign {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.textLines[index] = it.toPlainText()
		}

		return this
	}

	private fun updateNBTTagCompound() {
		this.lines.forEachIndexed { index, iChatBaseComponent ->
			nbtTagCompound.setString(
				"Text${index + 1}",
				IChatBaseComponent.ChatSerializer.a(
					iChatBaseComponent
				)
			)
		}

		nbtTagCompound.setInt("x", this.position.x)
		nbtTagCompound.setInt("y", this.position.y)
		nbtTagCompound.setInt("z", this.position.z)
		nbtTagCompound.setString("id", "minecraft:sign")
	}

}