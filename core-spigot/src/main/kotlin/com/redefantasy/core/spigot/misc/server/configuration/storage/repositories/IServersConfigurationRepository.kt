package com.redefantasy.core.spigot.misc.server.configuration.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.spigot.misc.server.configuration.data.ServerConfiguration
import com.redefantasy.core.spigot.misc.server.configuration.storage.dto.FetchServerConfigurationByServerNameDTO

/**
 * @author Gutyerrez
 */
interface IServersConfigurationRepository : IRepository {

	fun fetchServerConfigurationByServerName(
		fetchServerConfigurationByServerNameDTO: FetchServerConfigurationByServerNameDTO
	): ServerConfiguration?

}