package com.redefantasy.core.shared.misc.preferences.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.redefantasy.core.shared.misc.preferences.PreferenceState

/**
 * @author SrGutyerrez
 **/
abstract class Preference(
    @JsonProperty val name: String,
) {

    @JsonProperty var preferenceState: PreferenceState = PreferenceState.ENABLED

    abstract fun <T> getIcon(): T

}

