package com.redefantasy.core.spigot.misc.skin.inventory

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.misc.utils.DateFormatter
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.spigot.inventory.CustomInventory
import com.redefantasy.core.spigot.misc.player.openBook
import com.redefantasy.core.spigot.misc.player.openSignEditor
import com.redefantasy.core.spigot.misc.utils.BlockColor
import com.redefantasy.core.spigot.misc.utils.BookBuilder
import com.redefantasy.core.spigot.misc.utils.ItemBuilder
import com.redefantasy.core.spigot.sign.CustomSign
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author Gutyerrez
 */
class SkinsInventory(
	user: User
) : CustomInventory("Suas peles") {

	init {
		CoreProvider.Cache.Local.USERS_SKINS.provide().fetchByUserId(user.id)?.stream()
			?.sorted { o1, o2 ->
				o2.enabled.compareTo(o1.enabled) + o2.updatedAt.compareTo(o1.updatedAt)
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

		this.setItem(
			48,
			ItemBuilder(Material.BOOK_AND_QUILL)
				.name(
					"§eEscolher uma nova pele"
				).lore(
					arrayOf(
						"§7Você pode escolher uma nova pele",
						"§7para ser utilizada em sua conta.",
						"",
						"§fComando: §7/skin <nome>",
						"",
						"§aClique para escolher"
					)
				).build()
		) { event ->
			val player = event.whoClicked as Player

			val sign = CustomSign()
				.lines(
					TextComponent("§aTeste"),
					TextComponent(""),
					TextComponent("§cOpa")
				)

			player.openSignEditor(sign)
		}

		this.setItem(
			49,
			ItemBuilder(Material.BARRIER)
				.name(
					"§eAtualizar pele"
				).lore(
					arrayOf(
						"§7Isso irá restaurar a sua pele para a pele",
						"§7utilizada em sua conta do Minecraft. Caso",
						"§7você não possua uma conta, ficará a",
						"§7pele padrão do Minecraft.",
						"",
						"§fComando: §7/skin atualizar",
						"",
						"§aClique para atualizar."
					)
				).build()
		) { event ->
			val player = event.whoClicked as Player

			val book = BookBuilder()
				.title("Daleee")
				.author("Gutyerrez")
				.pages(
					TextComponent("§aOpa!")
				).build()

			player.openBook(book)
		}

		this.setItem(
			50,
			ItemBuilder(Material.SKULL_ITEM)
				.durability(3)
				.skull(
					BlockColor.YELLOW
				).name(
					"§eAjuda"
				).lore(
					arrayOf(
						"§7As ações disponíveis neste menu também",
						"§7podem ser realizadas por comando.",
						"",
						"§fComando: §7/skin ajuda",
						"",
						"§aClique para listar os comandos."
					)
				).build()
		)
	}

}