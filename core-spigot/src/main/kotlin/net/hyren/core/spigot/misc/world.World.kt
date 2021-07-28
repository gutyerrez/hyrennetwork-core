package net.hyren.core.spigot.misc

import org.bukkit.World
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld

fun World.asNMSWorld() = (this as CraftWorld).handle
