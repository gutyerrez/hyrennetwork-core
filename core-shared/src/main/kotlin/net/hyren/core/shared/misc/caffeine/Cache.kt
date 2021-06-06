package net.hyren.core.shared.misc.caffeine

import com.github.benmanes.caffeine.cache.LoadingCache

/**
 * @author Gutyerrez
 */
inline fun <reified T> LoadingCache<Any, T>.get(): T? = this.get("")