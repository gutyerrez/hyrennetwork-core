package com.redefantasy.core.spigot.inventory

import com.redefantasy.core.spigot.misc.utils.ItemBuilder
import kotlinx.coroutines.Runnable
import net.minecraft.server.v1_8_R3.ChatComponentText
import net.minecraft.server.v1_8_R3.EntityHuman
import net.minecraft.server.v1_8_R3.IInventory
import net.minecraft.server.v1_8_R3.ItemStack
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.function.Consumer

/**
 * @author Gutyerrez
 */
open class CustomInventory(
    title: String,
    size: Int
) : CraftInventory(
    MinecraftInventory(title, size)
), ICustomInventory {

    private val LISTENERS = mutableMapOf<Int, ICustomInventory.ClickListener>()

    private var onClose: Consumer<InventoryCloseEvent>? = null
    private var onOpen: Consumer<InventoryOpenEvent>? = null

    private val BACK_ARROW = ItemBuilder(Material.ARROW)
        .name("§aVoltar")
        .lore(
            arrayOf("", "§eClique para voltar.")
        )
        .build()

    init {
        (this.inventory as MinecraftInventory).init(this)
    }

    override fun <T : ICustomInventory.ClickListener> getListener(
        slot: Int
    ): T? = this.LISTENERS[slot] as T?

    override fun clear() {
        super.clear()

        this.LISTENERS.clear()
    }

    override fun clear(index: Int) {
        super.clear(index)

        this.LISTENERS.remove(index)
    }

    override fun setItem(
        slot: Int,
        itemStack: org.bukkit.inventory.ItemStack?
    ) {
        super.setItem(slot, itemStack)

        if (itemStack === null) {
            this.LISTENERS.remove(slot)
        }
    }

    fun setItem(
        y: Int,
        x: Int,
        itemStack: org.bukkit.inventory.ItemStack?
    ) {
        this.setItem(y * 9 + x, itemStack)
    }

    override fun setItem(
        slot: Int,
        itemStack: org.bukkit.inventory.ItemStack?,
        callback: Consumer<InventoryClickEvent>?
    ) {
        this.setItem(slot, itemStack)

        if (itemStack !== null && callback !== null) {
            this.LISTENERS[slot] = object : ICustomInventory.ConsumerClickListener {
                override fun accept(event: InventoryClickEvent) {
                    callback.accept(event)
                }
            }
        }
    }

    fun setItem(
        slot: Int,
        itemStack: org.bukkit.inventory.ItemStack?,
        callback: Runnable?
    ) {
        this.setItem(slot, itemStack, object : ICustomInventory.RunnableClickListener {
            override fun run() {
                if (callback !== null) {
                    callback.run()
                }
            }
        })
    }

    override fun addItem(
        itemStack: org.bukkit.inventory.ItemStack,
        callback: Consumer<InventoryClickEvent>
    ) {
        for (i in 0 until this.size) {
            if (this.contents[i] === null || this.contents[i].type === Material.AIR) {
                this.setItem(i, itemStack, callback)
                break
            }
        }
    }

    fun addItem(
        itemStack: org.bukkit.inventory.ItemStack,
        callback: Runnable
    ) {
        this.addItem(itemStack, object : ICustomInventory.RunnableClickListener {
            override fun run() {
                callback.run()
            }
        })
    }

    override fun on(
        event: InventoryOpenEvent
    ) {
        if (this.onOpen !== null)
            this.onOpen!!.accept(event)
    }

    override fun on(
        event: InventoryCloseEvent
    ) {
        if (this.onClose !== null)
            this.onClose!!.accept(event)
    }

    override fun backItem(
        callback: Consumer<InventoryClickEvent>
    ) {
        this.backItem(this.size - 5, callback)
    }

    override fun backItem(
        slot: Int,
        callback: Consumer<InventoryClickEvent>
    ) {
        this.setItem(
            slot,
            this.BACK_ARROW,
            callback
        )
    }

    override fun backItem(
        inventory: Inventory
    ) {
        this.backItem(this.size - 5, inventory)
    }

    override fun backItem(
        slot: Int,
        inventory: Inventory
    ) {
        this.setItem(
            slot,
            this.BACK_ARROW,
            Consumer<InventoryClickEvent> {
                it.whoClicked.openInventory(inventory)
            }
        )
    }

    class MinecraftInventory(
        private var title: String?,
        size: Int
    ) : Container(), IInventory {

        var items: Array<ItemStack?> = arrayOfNulls(size)

        private var maxStackSize = IInventory.MAX_STACK

        private val viewers = mutableListOf<HumanEntity>()

        private val type: InventoryType = InventoryType.CHEST

        override fun getMaxStackSize() = this.maxStackSize

        override fun getName() = this.title

        override fun hasCustomName() = this.title !== null

        override fun getScoreboardDisplayName() = ChatComponentText(this.title)

        override fun getSize() = this.items.size

        override fun getItem(p0: Int) = this.items[p0]

        override fun splitStack(
            p0: Int,
            p1: Int
        ): ItemStack? {
            val stack = this.getItem(p0)

            lateinit var result: ItemStack

            if (stack === null) return null

            if (stack.count <= p1) {
                this.setItem(p0, null)
            } else {
                result = CraftItemStack.copyNMSStack(stack, p1)

                stack.count -= p1
            }

            this.update()

            return result
        }

        override fun splitWithoutUpdate(
            p0: Int
        ): ItemStack? {
            val stack = this.getItem(p0)

            lateinit var result: ItemStack

            if (stack === null) return null

            if (stack.count <= 1) {
                this.setItem(p0, null)
            } else {
                result = CraftItemStack.copyNMSStack(stack, 1)

                stack.count -= 1
            }

            return result
        }

        override fun setItem(
            p0: Int,
            p1: ItemStack?
        ) {
            this.items[p0] = p1

            if (p1 !== null && this.maxStackSize > 0 && p1.count > this.maxStackSize) {
                p1.count = this.maxStackSize
            }
        }

        override fun update() {
            //
        }

        override fun a(
            p0: EntityHuman?
        ) = true

        override fun startOpen(
            p0: EntityHuman?
        ) {
            //
        }

        override fun closeContainer(
            p0: EntityHuman?
        ) {
            //
        }

        override fun b(
            p0: Int,
            p1: ItemStack?
        ) = true

        override fun b(
            p0: Int,
            p1: Int
        ) {
            //
        }

        override fun getProperty(
            p0: Int
        ) = 0

        override fun g() = 0

        override fun l() {
            //
        }

        override fun getContents() = this.items

        override fun onOpen(
            p0: CraftHumanEntity
        ) {
            this.viewers.add(p0)
        }

        override fun onClose(
            p0: CraftHumanEntity
        ) {
            this.viewers.remove(p0)
        }

        override fun getViewers() = this.viewers

        override fun getOwner(): InventoryHolder? = null

        override fun setMaxStackSize(p0: Int) {
            this.maxStackSize = p0
        }

    }

}