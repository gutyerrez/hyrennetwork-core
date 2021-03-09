package com.redefantasy.core.shared.providers.databases.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.redefantasy.core.shared.providers.databases.IDatabaseProvider
import com.redefantasy.core.shared.world.location.SerializedLocation
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap

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
                        CustomCodecProvider,
                        PojoCodecProvider.builder()
                            .register(SerializedLocation::class.java)
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

internal object CustomCodecProvider : CodecProvider {

    private val customCodecMap = ConcurrentHashMap<Class<*>, Codec<*>>()

    fun <T> addCustomCodec(codec: Codec<T>) {
        customCodecMap[codec.encoderClass] = codec
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> get(
        clazz: Class<T>,
        registry: CodecRegistry
    ): Codec<T>? = customCodecMap[clazz] as? Codec<T>

}