package net.hyren.core.shared.misc.maintenance.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.cache.local.LocalCache
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class MaintenanceLocalCache : LocalCache {

	private val CACHE = Caffeine.newBuilder()
		.expireAfterWrite(15, TimeUnit.SECONDS)
		.build<Application, Boolean> {
			CoreProvider.Repositories.PostgreSQL.MAINTENANCE_REPOSITORY.provide().fetchByApplication(it)
		}

	fun fetch(
		application: Application
	) = this.CACHE.get(application)

}