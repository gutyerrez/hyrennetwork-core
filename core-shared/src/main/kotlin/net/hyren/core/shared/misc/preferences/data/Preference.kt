package net.hyren.core.shared.misc.preferences.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.hyren.core.shared.misc.preferences.PreferenceState

/**
 * @author SrGutyerrez
 **/
@Serializable
data class Preference(
    val name: String,
    @SerialName("preference_state")
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
