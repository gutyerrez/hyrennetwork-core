package com.redefantasy.core.shared.providers.databases.mongo.repositories

import com.mongodb.client.MongoCollection
import com.redefantasy.core.shared.providers.databases.mongo.MongoDatabaseProvider
import kotlin.reflect.KClass

/**
 * @author SrGutyerrez
 **/
open class MongoRepository<T : Any>(
    private val mongoDatabaseProvider: MongoDatabaseProvider,
    private val collectionName: String,
    private val tKlass: KClass<T>
) {

    var mongoCollection: MongoCollection<T> = this.mongoDatabaseProvider.provide().getCollection(
        this.collectionName,
        this.tKlass.java
    )

}