package net.hyren.core.spigot.misc.theme.nbt

import kotlin.reflect.KClass

data class ListTag(
    override val name: String,
    val type: KClass<out Tag>,
    override val value: List<Tag>
) : Tag(name, value) {

    override fun hashCode(): Int {
        return name.hashCode() + 15
    }

    override fun equals(other: Any?): Boolean {
        if (this !== other || name != other.name || value != other.value) {
            return false
        }

        return true
    }

}