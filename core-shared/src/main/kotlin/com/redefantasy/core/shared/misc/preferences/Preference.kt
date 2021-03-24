package com.redefantasy.core.shared.misc.preferences

import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.misc.minecraft.material.Material
import java.io.Serializable

/**
 * @author SrGutyerrez
 **/
abstract class Preference(
    @JsonProperty
    open val name: String,
    @JsonProperty("preference_state")
    var preferenceState: PreferenceState = PreferenceState.ENABLED
) : Serializable {

    abstract fun getIcon(): PreferenceIcon

}

open class PreferenceIcon(
    val material: Material,
    val displayName: String,
    val lore: Array<String> = emptyArray()
) : Serializable