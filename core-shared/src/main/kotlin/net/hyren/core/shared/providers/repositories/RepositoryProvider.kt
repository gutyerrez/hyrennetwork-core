package net.hyren.core.shared.providers.repositories

import net.hyren.core.shared.providers.IProvider
import net.hyren.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
abstract class RepositoryProvider<T: IRepository>(
        val databaseProvider: Any,
        val repository: T
) : IProvider<T>