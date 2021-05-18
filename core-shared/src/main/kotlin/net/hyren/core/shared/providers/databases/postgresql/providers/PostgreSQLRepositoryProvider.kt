package net.hyren.core.shared.providers.databases.postgresql.providers

import net.hyren.core.shared.providers.IProvider
import net.hyren.core.shared.storage.repositories.IRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * @author SrGutyerrez
 **/
class PostgreSQLRepositoryProvider<T : IRepository>(
    private val repositoryClass: KClass<out T>
) : IProvider<T> {

    private lateinit var t: T

    override fun prepare() {
        this.t = this.repositoryClass.createInstance()
    }

    override fun provide() = t

}
