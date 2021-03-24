package com.redefantasy.core.shared.misc.preferences.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.misc.minecraft.material.Material
import com.redefantasy.core.shared.misc.preferences.Preference
import com.redefantasy.core.shared.misc.preferences.PreferenceIcon
import com.redefantasy.core.shared.misc.preferences.PreferenceState

/**
 * @author Gutyerrez
 */
data class TellPreference(
    @JsonProperty
    override val name: String = "user-private-messages-preference",
    @JsonProperty("preference_state")
    override var preferenceState: PreferenceState = PreferenceState.ENABLED
) : Preference {

    override fun getIcon() = PreferenceIcon(
        Material.EMPTY_MAP,
        "${this.preferenceState.getColor()}Mensagens privadas",
        arrayOf("§7Receber mensagens privadas.")
    )

}