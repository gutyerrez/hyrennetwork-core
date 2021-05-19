package net.hyren.core.shared.misc.preferences

import net.hyren.core.shared.misc.preferences.data.Preference

/**
 * @author Gutyerrez
 */
object PreferenceRegistry {

    private val PREFERENCES = mutableMapOf<String, Preference>()

    init {
        register(
            TELL_PREFERENCE,
            PLAYER_VISIBILITY
        )
    }

    fun register(vararg preferences: Preference) {
        preferences.forEach {
            PREFERENCES[it.name] = it
        }
    }

    fun fetchAll() = PREFERENCES.values.toTypedArray()

    fun fetchByName(name: String) = PREFERENCES[name]

}

val TELL_PREFERENCE = Preference("user-private-messages-preference")

val PLAYER_VISIBILITY = Preference("player-visibility-preference")

val FLY_IN_LOBBY = Preference("fly-in-lobby-preference", PreferenceState.DISABLED)

val LOBBY_COMMAND_PROTECTION = Preference("lobby-command-protection-preference")
