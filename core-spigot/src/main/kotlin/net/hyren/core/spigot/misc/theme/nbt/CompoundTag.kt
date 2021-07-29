package net.hyren.core.spigot.misc.theme.nbt

data class CompoundTag(
    override val name: String,
    override val value: Map<String,Tag<*>>
): Tag<Map<String, Tag<*>>>(name, value) {
}