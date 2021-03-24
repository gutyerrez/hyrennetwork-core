package com.redefantasy.core.shared.misc.preferences

import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.misc.minecraft.material.Material

/**
 * @author SrGutyerrez
 **/
open class Preference(
    @JsonProperty open val name: String,
    @JsonProperty open var preferenceState: PreferenceState = PreferenceState.ENABLED
) {

    open fun getIcon(): PreferenceIcon {
        TODO("auto-generated method")
    }

    override fun toString(): String = CoreConstants.GSON.toJson(this)

}

open class PreferenceIcon(
    val material: Material,
    val displayName: String,
    val lore: Array<String> = emptyArray()
)