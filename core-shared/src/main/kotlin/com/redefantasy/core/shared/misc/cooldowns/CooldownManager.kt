package com.redefantasy.core.shared.misc.cooldowns

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.misc.cooldowns.data.Cooldown
import com.redefantasy.core.shared.users.data.User
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

/**
 * @author SrGutyerrez
 **/
class CooldownManager {

    private val CACHE = Caffeine.newBuilder()
            .build<EntityID<UUID>, Cooldown>()

    fun start(user: User, any: Any, duration: Long) {
        val cooldown = this.CACHE.getIfPresent(user.id) ?: Cooldown()

        cooldown.start(any, System.currentTimeMillis() + duration)

        this.CACHE.put(user.id, cooldown)
    }

    fun inCooldown(user: User, any: Any): Boolean {
        val cooldown = this.CACHE.getIfPresent(user.id) ?: Cooldown()

        return cooldown.inCooldown(any)
    }

    fun getRemainingTime(user: User, any: Any): Long {
        val cooldown = this.CACHE.getIfPresent(user.id) ?: Cooldown()

        return cooldown.getRemainingTime(any)
    }

}