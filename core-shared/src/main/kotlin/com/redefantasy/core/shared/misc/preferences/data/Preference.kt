package com.redefantasy.core.shared.misc.preferences.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.misc.preferences.PreferenceState

/**
 * @author SrGutyerrez
 **/
open class Preference(
    @JsonProperty open val name: String,
    @JsonProperty open var preferenceState: PreferenceState = PreferenceState.ENABLED
) {

    @JsonIgnore
    open fun <T> getIcon(): T {
        TODO("auto-generated method")
    }

    override fun toString(): String = CoreConstants.GSON.toJson(this)

}

