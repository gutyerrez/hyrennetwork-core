package com.redefantasy.core.shared.misc.preferences.data

import com.redefantasy.core.shared.misc.preferences.PreferenceState

/**
 * @author SrGutyerrez
 **/
abstract class Preference<T>(
    val name: String,
) {

    var preferenceState: PreferenceState = PreferenceState.ENABLED

    abstract fun getIcon(): T

}