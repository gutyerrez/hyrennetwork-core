package net.hyren.core.spigot.misc.theme.nbt

data class IntArrayTag(
    override val name: String,
    override val value: Array<Int>
) : Tag(name, value) {

    override fun hashCode(): Int {
        return name.hashCode() + 15
    }

    override fun equals(other: Any?): Boolean {
        if (this !== other || name != other.name || !value.contentEquals(other.value)) {
            return false
        }

        return true
    }

}