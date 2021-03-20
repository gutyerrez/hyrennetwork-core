package com.redefantasy.core.spigot.misc.preferences.tell

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.misc.preferences.PreferenceRegistry
import com.redefantasy.core.shared.misc.preferences.PreferenceState
import com.redefantasy.core.shared.misc.preferences.data.Preference
import com.redefantasy.core.shared.users.preferences.storage.dto.UpdateUserPreferencesDTO
import com.redefantasy.core.spigot.misc.utils.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
data class TellPreference(
    override val name: String = "user-private-messages-preference",
    override var preferenceState: PreferenceState = PreferenceState.ENABLED
) : Preference(
    name,
    preferenceState
) {

    @Suppress("UNCHECKED_CAST")
    override fun <T> getIcon(): T {
        return ItemBuilder(Material.EMPTY_MAP)
            .name("${this.preferenceState.getColor()}Mensagens privadas")
            .lore(
                arrayOf(
                    "§7Receber mensagens privadas."
                )
            )
            .build() as T
    }

    @Subscribe
    fun on(
        event: InventoryClickEvent
    ) {
        println("Chamou")

        val player = event.whoClicked as Player
        val user = CoreProvider.Cache.Local.USERS.provide().fetchById(player.uniqueId)!!

        var preferences = user.getPreferences()

        if (preferences.size != PreferenceRegistry.fetchAll().size) {
            preferences = preferences.plus(PreferenceRegistry.fetchAll())

            println(preferences)
        }

        val preference = preferences.find { it == this }!!

        if (CoreConstants.COOLDOWNS.inCooldown(user, this.name)) return

        println("Passou aqui")

        val currentPreferenceState = preference.preferenceState
        val switchPreferenceState = when (currentPreferenceState) {
            PreferenceState.ENABLED -> PreferenceState.DISABLED
            PreferenceState.DISABLED -> PreferenceState.ENABLED
        }

        preference.preferenceState = switchPreferenceState

        CoreProvider.Repositories.Postgres.USERS_PREFERENCES_REPOSITORY.provide().update(
            UpdateUserPreferencesDTO(
                user.id,
                preferences
            )
        )

        println("dale")

        CoreConstants.COOLDOWNS.start(user, this.name, TimeUnit.SECONDS.toMillis(3))
    }

}