package com.redefantasy.core.spigot.misc.skin.inventory

import com.redefantasy.core.spigot.inventory.CustomInventory
import com.redefantasy.core.spigot.misc.utils.ItemBuilder
import org.bukkit.Material

/**
 * @author Gutyerrez
 */
class SkinsInventory : CustomInventory("Suas peles") {

	init {
		for (i in 0..50) {
			this.addItem(
				ItemBuilder(Material.WOOL)
					.name("Item #$i")
					.build()
			)
		}
	}

}