package com.redefantasy.core.spigot.inventory

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.spigot.misc.utils.ItemBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * @author Gutyerrez
 */
class ConfirmInventory(
    val icon: ItemStack? = null,
    val onAccept: Consumer<InventoryClickEvent>,
    val onDeny: Consumer<InventoryClickEvent>
) {

    private var createTime: DateTime? = null

    fun createTime(
        createTime: DateTime
    ): ConfirmInventory {
        this.createTime = createTime

        return this
    }

    fun make(
        acceptDescription: Array<String>
    ): CustomInventory = ConfirmInventoryBuilder(acceptDescription).build()

    private inner class ConfirmInventoryBuilder(
        private val acceptDescription: Array<String>
    ) : CustomInventory(
        "Confirmação",
        if (this.icon === null) 3 * 9 else 4 * 9
    ) {

        fun build(): ConfirmInventoryBuilder {
            val accept = ItemBuilder(Material.WOOL)
                .durability(5)
                .name("§aAceitar (Leia abaixo)")
                .lore(this.acceptDescription)
                .build()

            val deny = ItemBuilder(Material.WOOL)
                .durability(14)
                .name("§cNegar")
                .lore(
                    arrayOf(
                        "§7Cancelar esta operação."
                    )
                )
                .build()

            this.setItem(
                if (this@ConfirmInventory.icon === null) {
                    11
                } else 20, accept
            ) { it ->
                val player = it.whoClicked as Player

                player.closeInventory()

                if (
                    this@ConfirmInventory.createTime !== null && this@ConfirmInventory.createTime!!.withMillis(
                        TimeUnit.SECONDS.toMillis(
                            15
                        )
                    ) < DateTime.now(
                        CoreConstants.DATE_TIME_ZONE
                    )
                ) {
                    player.sendMessage(TextComponent("§cVocê demorou muito para finalizar, tente novamente."))
                    return@setItem
                }

                this@ConfirmInventory.onAccept.accept(it)
            }

            this.setItem(
                if (this@ConfirmInventory.icon === null) {
                    15
                } else 24, deny
            ) { it ->
                val player = it.whoClicked as Player

                player.closeInventory()

                this@ConfirmInventory.onDeny.accept(it)
            }

            if (this@ConfirmInventory.icon !== null) {
                this.setItem(13, this@ConfirmInventory.icon.clone())
            }

            return this
        }

    }

}