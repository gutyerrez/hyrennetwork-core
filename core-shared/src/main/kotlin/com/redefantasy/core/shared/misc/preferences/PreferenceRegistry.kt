package com.redefantasy.core.shared.misc.preferences

import com.redefantasy.core.shared.misc.preferences.data.Preference

/**
 * @author Gutyerrez
 */
val TELL_PREFERENCE = Preference("user-private-messages-preference")

object PreferenceRegistry {

    private val PREFERENCES = mutableMapOf<String, Preference>()

    init {
        this.register(
            TELL_PREFERENCE
        )
    }

    fun register(vararg preferences: Preference) {
        preferences.forEach {
            this.PREFERENCES[it.name] = it
        }
    }

    fun fetchAll() = this.PREFERENCES.values.toTypedArray()

    fun fetchByName(name: String) = this.PREFERENCES[name]

}