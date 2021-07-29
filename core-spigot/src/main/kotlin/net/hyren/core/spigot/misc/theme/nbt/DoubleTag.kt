package net.hyren.core.spigot.misc.theme.nbt

data class DoubleTag(
    override val name: String,
    override val value: Double
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
