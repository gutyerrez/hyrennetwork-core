package com.redefantasy.core.spigot.misc.utils

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.server.v1_8_R3.ChatComponentText
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaBook
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
class BookBuilder {

	private val itemStack = ItemStack(Material.WRITTEN_BOOK)
	private val itemMeta = itemStack.itemMeta as CraftMetaBook

	fun title(title: String): BookBuilder {
		itemMeta.displayName = title

		return this
	}

	fun author(author: String): BookBuilder {
		itemMeta.author = author

		return this
	}

	@Deprecated("String pages are deprecated")
	fun pages(vararg pages: String): BookBuilder {
		pages.forEach {
			itemMeta.pages.add(
				ChatComponentText(it)
			)
		}

		return this
	}

	@Deprecated("String list pages are deprecated")
	fun pages(pages: List<String>): BookBuilder {
		pages.forEach {
			itemMeta.pages.add(
				ChatComponentText(it)
			)
		}

		return this
	}

	fun pages(vararg pages: Array<BaseComponent>): BookBuilder {
		pages.forEach {
			itemMeta.pages.add(
				IChatBaseComponent.ChatSerializer.a(
					ComponentSerializer.toString(it)
				)
			)
		}

		return this
	}

	fun pages(vararg pages: TextComponent): BookBuilder {
		pages.forEach {
			itemMeta.pages = listOf(
				IChatBaseComponent.ChatSerializer.a(
					ComponentSerializer.toString(it)
				)
			)
		}

		return this
	}

	fun build(): ItemStack {
		itemStack.itemMeta = itemMeta

		return itemStack
	}

}