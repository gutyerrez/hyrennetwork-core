package com.redefantasy.core.shared.providers.databases.mongo.repositories

import com.mongodb.client.MongoCollection
import com.redefantasy.core.shared.providers.databases.mongo.MongoDatabaseProvider
import kotlin.reflect.KClass

/**
 * @author SrGutyerrez
 **/
open class MongoRepository<T: Any>(
        val mongoDatabaseProvider: MongoDatabaseProvider,
        val collectionName: String,
        val tClass: KClass<T>
) {

    var mongoCollection: MongoCollection<T> = mongoDatabaseProvider.provide().getCollection(
            collectionName,
            tClass.java
    )

}