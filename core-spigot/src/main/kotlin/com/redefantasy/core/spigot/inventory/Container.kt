package com.redefantasy.core.spigot.inventory

/**
 * @author Gutyerrez
 */
open class Container {

    lateinit var parent: ICustomInventory

    fun init(parent: ICustomInventory) {
        this.parent = parent
    }

}