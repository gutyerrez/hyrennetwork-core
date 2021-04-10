package com.redefantasy.core.spigot.sign

import com.redefantasy.core.shared.misc.kotlin.sizedArray
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.minecraft.server.v1_8_R3.BlockPosition
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.TileEntitySign
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.function.BiConsumer

/**
 * @author Gutyerrez
 */
class CustomSign(
	blockPosition: BlockPosition? = null
) : TileEntitySign() {

	var UPDATED_LISTENER: BiConsumer<Player, Array<IChatBaseComponent>>? = null

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

	fun lines(vararg lines: String) = apply {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.textLines[index] = it
		}
	}

	fun lines(lines: Array<BaseComponent>) = apply {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.textLines[index] = it.toLegacyText()
		}
	}

	fun lines(vararg lines: TextComponent) = apply {
		if (lines.size > 4)
			throw IllegalArgumentException("The size of lines ${lines.size} is higher than 4")

		lines.forEachIndexed { index, it ->
			this.textLines[index] = it.toLegacyText()
		}
	}

	fun onUpdate(
		consumer: BiConsumer<Player, Array<IChatBaseComponent>>
	) = apply {
		this.UPDATED_LISTENER = consumer
	}

}