package com.redefantasy.core.shared.misc.servers.configuration.storage.repositories

import com.redefantasy.core.shared.misc.servers.configuration.data.ServerConfiguration
import com.redefantasy.core.shared.misc.servers.configuration.storage.dto.FetchServerConfigurationByServerNameDTO
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author Gutyerrez
 */
interface IServersConfigurationRepository<T> : IRepository {

	fun fetchServerConfigurationByServerName(
		fetchServerConfigurationByServerNameDTO: FetchServerConfigurationByServerNameDTO
	): ServerConfiguration<T>?

}