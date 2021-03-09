package com.redefantasy.core.shared.providers.databases.mongo.providers

import com.redefantasy.core.shared.providers.IProvider
import com.redefantasy.core.shared.storage.repositories.IRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * @author SrGutyerrez
 **/
class MongoRepositoryProvider<T: IRepository>(
        private val repositoryClass: KClass<out T>
) : IProvider<T> {

    private lateinit var t: T

    override fun prepare() {
        this.t = this.repositoryClass.createInstance()
    }

    override fun provide() = this.t
}