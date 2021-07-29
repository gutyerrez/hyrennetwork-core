package net.hyren.core.spigot.misc.theme.nbt

abstract class Tag<T>(
    open val name: String,
    open val value: T
)