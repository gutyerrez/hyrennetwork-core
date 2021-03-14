package com.redefantasy.core.spigot.listeners

import com.redefantasy.core.spigot.inventory.CustomInventory
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory
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
        val inventory = event.inventory
        val craftInventory = inventory as CraftInventory
        val iInventory = craftInventory.inventory

        println("b")

        if (iInventory is CustomInventory.MinecraftInventory) {
            println("bb")

            iInventory.parent.on(event)
        }
    }

    @EventHandler
    fun on(
        event: InventoryCloseEvent
    ) {
        val inventory = event.inventory
        val craftInventory = inventory as CraftInventory
        val iInventory = craftInventory.inventory

        if (iInventory is CustomInventory.MinecraftInventory) {
            iInventory.parent.on(event)
        }
    }

    @EventHandler
    fun on(
        event: InventoryOpenEvent
    ) {
        val inventory = event.inventory
        val craftInventory = inventory as CraftInventory
        val iInventory = craftInventory.inventory

        if (iInventory is CustomInventory.MinecraftInventory) {
            iInventory.parent.on(event)
        }
    }

}