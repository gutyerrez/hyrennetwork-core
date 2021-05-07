package net.hyren.core.shared.misc.cooldowns.data

import com.github.benmanes.caffeine.cache.Caffeine

/**
 * @author SrGutyerrez
 **/
class Cooldown {

    private val CACHE = Caffeine.newBuilder()
            .build<Any, Long>()

    fun start(any: Any, duration: Long) {
        this.CACHE.put(any, duration)
    }

    fun inCooldown(any: Any): Boolean {
        return (this.CACHE.getIfPresent(any) ?: 0) > System.currentTimeMillis()
    }

    fun getRemainingTime(any: Any): Long {
        return (this.CACHE.getIfPresent(any) ?: 0) - System.currentTimeMillis()
    }

}