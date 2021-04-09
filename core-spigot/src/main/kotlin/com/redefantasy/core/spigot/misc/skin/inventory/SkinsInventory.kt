package com.redefantasy.core.spigot.misc.skin.inventory

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.misc.utils.DateFormatter
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.spigot.inventory.CustomInventory
import com.redefantasy.core.spigot.misc.utils.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class SkinsInventory(
	user: User
) : CustomInventory("Suas peles") {

	init {
		CoreProvider.Cache.Local.USERS_SKINS.provide().fetchByUserId(user.id)?.sortedBy {
			it.updatedAt
		}?.forEach {
			this.addItem(
				ItemBuilder(Material.SKULL_ITEM)
					.durability(3)
					.skull(it.skin)
					.name(
						"§a${it.name}"
					).lore(
						arrayOf(
							"§fUsada pela última  vez em: §7${
								DateFormatter.formatToDefault(
									it.updatedAt,
									"às"
								)
							}",
							"",
							if (it.enabled) "§aSelecionada." else "§eClique para utilizar essa pele."
						)
					)
					.build()
			) { event ->
				val player = event.whoClicked as Player

				player.closeInventory()

				player.sendMessage("Dale papi !")
			}
		}
	}

}