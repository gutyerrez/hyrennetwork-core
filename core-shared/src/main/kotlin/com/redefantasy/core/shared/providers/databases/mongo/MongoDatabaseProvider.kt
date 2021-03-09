package com.redefantasy.core.shared.providers.databases.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.redefantasy.core.shared.providers.databases.IDatabaseProvider
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.pojo.PojoCodecProvider
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
class MongoDatabaseProvider(
    private val address: InetSocketAddress,
    private val user: String,
    private val password: String,
    private val database: String
) : IDatabaseProvider<MongoDatabase> {

    private lateinit var mongoClient: MongoClient
    private lateinit var mongoDatabase: MongoDatabase

    override fun prepare() {
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(
                ConnectionString(
                    "mongodb://${this.address.address.hostAddress}:${this.address.port}"
                )
            )
            .credential(
                MongoCredential.createCredential(
                    this.user,
                    "admin",
                    this.password.toCharArray()
                )
            )
            .codecRegistry(
                CodecRegistries.fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(
                        PojoCodecProvider.builder()
                            .build()
                    ),
                    CodecRegistries.fromCodecs(
                        MongoClientSettings.getDefaultCodecRegistry().get(
                            Document::class.java
                        )
                    )
                )
            )
            .build()

        this.mongoClient = MongoClients.create(mongoClientSettings)

        this.mongoDatabase = mongoClient.getDatabase(this.database)
    }

    override fun provide() = this.mongoDatabase

    override fun shutdown() = this.mongoClient.close()

}