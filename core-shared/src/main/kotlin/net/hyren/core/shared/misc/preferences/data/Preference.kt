package net.hyren.core.shared.misc.preferences.data

import net.hyren.core.shared.misc.preferences.PreferenceState

/**
 * @author SrGutyerrez
 **/
data class Preference(
    val name: String,
    var preferenceState: PreferenceState = PreferenceState.ENABLED
) {

    fun getStateColor() = preferenceState.getColor()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is Preference) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return this.name.hashCode()
    }

}
