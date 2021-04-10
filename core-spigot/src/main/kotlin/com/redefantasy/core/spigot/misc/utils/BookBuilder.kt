package com.redefantasy.core.spigot.misc.utils

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.chat.ComponentSerializer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

/**
 * @author Gutyerrez
 */
class BookBuilder {

	private val itemStack = ItemStack(Material.WRITTEN_BOOK)
	private val itemMeta = itemStack.itemMeta as BookMeta

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
		itemMeta.setPages(*pages)

		return this
	}

	@Deprecated("String list pages are deprecated")
	fun pages(pages: List<String>): BookBuilder {
		itemMeta.pages = pages

		return this
	}

	fun pages(pages: Array<BaseComponent>): BookBuilder {
		pages.forEach {
			itemMeta.pages = listOf(
				ComponentSerializer.toString(it)
			)
		}

		return this
	}

	fun pages(vararg pages: TextComponent): BookBuilder {
		pages.forEach {
			itemMeta.pages = listOf(
				ComponentSerializer.toString(it)
			)
		}

		return this
	}

	fun build(): ItemStack {
		itemStack.itemMeta = itemMeta

		return itemStack
	}

}