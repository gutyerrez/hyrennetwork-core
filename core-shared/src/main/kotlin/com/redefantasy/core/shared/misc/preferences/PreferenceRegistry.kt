package com.redefantasy.core.shared.misc.preferences

/**
 * @author Gutyerrez
 */
object PreferenceRegistry {

    private val PREFERENCES = mutableMapOf<String, Preference>()

    init {
        this.register(
            Preference("user-private-messages-preference")
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