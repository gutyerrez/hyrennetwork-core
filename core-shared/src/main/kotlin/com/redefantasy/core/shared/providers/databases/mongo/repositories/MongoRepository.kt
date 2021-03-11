package com.redefantasy.core.shared.providers.databases.mongo.repositories

import com.redefantasy.core.shared.providers.databases.mongo.MongoDatabaseProvider
import org.bson.UuidRepresentation
import org.mongojack.JacksonMongoCollection
import kotlin.reflect.KClass

/**
 * @author SrGutyerrez
 **/
open class MongoRepository<T : Any>(
    private val mongoDatabaseProvider: MongoDatabaseProvider,
    private val collectionName: String,
    private val tKlass: KClass<T>
) {

    val mongoCollection = JacksonMongoCollection.builder()
        .build(
            this.mongoDatabaseProvider.provide(),
            this.collectionName,
            this.tKlass.java,
            UuidRepresentation.JAVA_LEGACY
        )

}