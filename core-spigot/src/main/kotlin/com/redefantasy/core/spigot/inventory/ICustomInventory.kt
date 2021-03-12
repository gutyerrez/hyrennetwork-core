package com.redefantasy.core.spigot.inventory

import org.bukkit.entity.Player
import org.bukkit.event.inventory.*
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

/**
 * @author Gutyerrez
 */
interface ICustomInventory {

    fun getSize(): Int

    fun <T: ClickListener> getListener(slot: Int): T?

    fun setItem(
        slot: Int,
        itemStack: ItemStack?
    )

    fun setItem(
        slot: Int,
        itemStack: ItemStack?,
        callback: Consumer<InventoryClickEvent>?
    )

    fun addItem(
        itemStack: ItemStack,
        callback: Consumer<InventoryClickEvent>
    )

    fun backItem(
        callback: Consumer<InventoryClickEvent>
    )

    fun backItem(
        slot: Int,
        callback: Consumer<InventoryClickEvent>
    )

    fun backItem(
        inventory: Inventory
    )

    fun backItem(
        slot: Int,
        inventory: Inventory
    )

    fun on(
        event: InventoryClickEvent
    ) {
        if (event.whoClicked is Player) {
            event.isCancelled = true

            if (event.clickedInventory !== null && event.clickedInventory.type !== InventoryType.PLAYER) {
                val clickListener = this.getListener<ClickListener>(event.slot)

                if (clickListener !== null) {
                    if (clickListener is ConsumerClickListener) {
                        clickListener.accept(event)
                    } else if (clickListener is RunnableClickListener) {
                        clickListener.run()
                    }
                }
            }
        }
    }

    fun on(
        event: InventoryDragEvent
    ) {
        //
    }

    fun on(
        event: InventoryOpenEvent
    ) {
        //
    }

    fun on(
        event: InventoryCloseEvent
    ) {
        //
    }

    interface ClickListener

    interface ConsumerClickListener : ClickListener, Consumer<InventoryClickEvent>

    interface RunnableClickListener : ClickListener, Runnable

}