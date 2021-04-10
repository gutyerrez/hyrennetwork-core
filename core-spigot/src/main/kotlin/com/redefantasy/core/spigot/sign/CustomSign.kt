package com.redefantasy.core.spigot.sign

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.server.v1_8_R3.BlockPosition
import net.minecraft.server.v1_8_R3.ChatComponentText
import net.minecraft.server.v1_8_R3.TileEntitySign
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class CustomSign(
	blockPosition: BlockPosition? = null
) : TileEntitySign() {

	constructor(player: Player): this(
		player.location
	)

	constructor(location: Location): this(
		BlockPosition(
			location.blockX,
			location.blockY,
			location.blockZ
		)
	)

	init {
		this.position = blockPosition
	}

	fun lines(vararg lines: String): CustomSign {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.lines[index] = ChatComponentText(it)
		}

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

		return this
	}

}