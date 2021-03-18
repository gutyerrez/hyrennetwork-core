package com.redefantasy.core.shared.misc.preferences

import com.redefantasy.core.shared.misc.preferences.data.Preference
import org.greenrobot.eventbus.EventBus

/**
 * @author Gutyerrez
 */
object PreferenceRegistry {

    private val PREFERENCES = mutableMapOf<String, Preference<*>>()

    val BUS = mutableMapOf<String, EventBus>()

    fun register(vararg preferences: Preference<*>) {
        preferences.forEach {
            this.PREFERENCES[it.name] = it

            val bus = EventBus.builder()
                .logNoSubscriberMessages(false)
                .logSubscriberExceptions(true)
                .build()

            this.BUS[it.name] = bus
        }
    }

    fun fetchAll() = this.PREFERENCES.values.toTypedArray()

    fun fetchByName(name: String) = this.PREFERENCES[name]

}