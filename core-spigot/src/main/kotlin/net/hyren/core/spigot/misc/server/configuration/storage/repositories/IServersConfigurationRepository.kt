package net.hyren.core.spigot.misc.server.configuration.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.spigot.misc.server.configuration.data.ServerConfiguration
import net.hyren.core.spigot.misc.server.configuration.storage.dto.FetchServerConfigurationByServerNameDTO

/**
 * @author Gutyerrez
 */
interface IServersConfigurationRepository : IRepository {

	fun fetchServerConfigurationByServerName(
		fetchServerConfigurationByServerNameDTO: FetchServerConfigurationByServerNameDTO
	): ServerConfiguration?

}