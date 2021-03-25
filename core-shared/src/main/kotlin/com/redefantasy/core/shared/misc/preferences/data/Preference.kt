package com.redefantasy.core.shared.misc.preferences

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.misc.minecraft.material.Material
import java.io.Serializable

/**
 * @author SrGutyerrez
 **/
data class Preference(
    @JsonProperty
    val name: String,

) : Serializable {

    @JsonProperty("preference_state")
    var preferenceState: PreferenceState = PreferenceState.ENABLED

    fun getStateColor() = preferenceState.getColor()

    @JsonIgnore
    val icon = when (this.name) {
        "user-private-messages-preference" -> PreferenceIcon(
            Material.EMPTY_MAP,
            "${this.getStateColor()}Mensagens privadas",
            arrayOf("§7Receber mensagens privadas.")
        )
        else -> null
    }

}

open class PreferenceIcon(
    val material: Material,
    val displayName: String,
    val lore: Array<String> = emptyArray()
) : Serializable