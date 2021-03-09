package com.redefantasy.core.shared.providers.databases.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.redefantasy.core.shared.misc.preferences.data.Preference
import com.redefantasy.core.shared.providers.databases.IDatabaseProvider
import com.redefantasy.core.shared.users.friends.data.FriendUser
import com.redefantasy.core.shared.users.ignored.data.IgnoredUser
import com.redefantasy.core.shared.world.location.SerializedLocation
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
                            .register(
                                SerializedLocation::class.java,
                                FriendUser::class.java,
                                IgnoredUser::class.java,
                                Preference::class.java
                            )
                            .automatic(true)
                            .build()
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