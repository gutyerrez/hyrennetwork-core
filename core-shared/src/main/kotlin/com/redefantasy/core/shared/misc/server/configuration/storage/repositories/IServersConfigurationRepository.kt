package com.redefantasy.core.shared.misc.server.configuration.storage.repositories

import com.redefantasy.core.shared.misc.server.configuration.data.ServerConfiguration
import com.redefantasy.core.shared.misc.server.configuration.storage.dto.FetchServerConfigurationByServerNameDTO
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author Gutyerrez
 */
interface IServersConfigurationRepository : IRepository {

	fun <T> fetchServerConfigurationByServerName(
		fetchServerConfigurationByServerNameDTO: FetchServerConfigurationByServerNameDTO
	): ServerConfiguration<T>?

}