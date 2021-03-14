package com.redefantasy.core.spigot.listeners

import com.redefantasy.core.spigot.inventory.CustomInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

/**
 * @author Gutyerrez
 */
class GeneralListener : Listener {

    @EventHandler
    fun on(
        event: InventoryClickEvent
    ) {
        val clickedInventory = event.clickedInventory

        if (clickedInventory === null) return

        println("aaa")

        if (clickedInventory is CustomInventory) {
            println("bb")

            clickedInventory.on(event)
        }
    }

    @EventHandler
    fun on(
        event: InventoryCloseEvent
    ) {
        val closedInventory = event.inventory

        if (closedInventory === null) return

        if (closedInventory is CustomInventory) {
            closedInventory.on(event)
        }
    }

    @EventHandler
    fun on(
        event: InventoryOpenEvent
    ) {
        val openedInventory = event.inventory

        if (openedInventory === null) return

        if (openedInventory is CustomInventory) {
            openedInventory.on(event)
        }
    }

}