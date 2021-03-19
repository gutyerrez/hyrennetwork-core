package com.redefantasy.core.shared.misc.preferences.data

import com.redefantasy.core.shared.misc.preferences.PreferenceState

/**
 * @author SrGutyerrez
 **/
abstract class Preference(
    val name: String,
) {

    var preferenceState: PreferenceState = PreferenceState.ENABLED

    abstract fun <T> getIcon(): T

}

