package com.redefantasy.core.shared.providers.repositories

import com.redefantasy.core.shared.providers.IProvider
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
abstract class RepositoryProvider<T: IRepository>(
        val databaseProvider: Any,
        val repository: T
) : IProvider<T>