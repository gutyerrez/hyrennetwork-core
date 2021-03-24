package com.redefantasy.core.shared.misc.preferences.data

import com.redefantasy.core.shared.misc.minecraft.material.Material
import com.redefantasy.core.shared.misc.preferences.Preference
import com.redefantasy.core.shared.misc.preferences.PreferenceIcon
import com.redefantasy.core.shared.misc.preferences.PreferenceState

/**
 * @author Gutyerrez
 */
data class TellPreference(
    override val name: String = "user-private-messages-preference",
    override var preferenceState: PreferenceState = PreferenceState.ENABLED
) : Preference(
    name,
    preferenceState
) {

    override fun getIcon() = PreferenceIcon(
        Material.EMPTY_MAP,
        "${this.preferenceState.getColor()}Mensagens privadas",
        arrayOf("§7Receber mensagens privadas.")
    )

}