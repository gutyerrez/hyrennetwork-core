package net.hyren.core.shared.misc.kotlin

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.status.ApplicationStatus
import net.hyren.core.shared.misc.skin.controller.SkinController
import net.hyren.core.shared.servers.data.Server
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.joda.time.DateTime
import java.net.InetSocketAddress
import java.util.*

/**
 * @author Gutyerrez
 */
object ApplicationStatusSerializer : KSerializer<ApplicationStatus> {
    override val descriptor: SerialDescriptor = ContextualSerializer(
        SkinController.MinecraftProfileData::class,
        null,
        emptyArray()
    ).descriptor

    override fun serialize(
        encoder: Encoder,
        value: ApplicationStatus
    ) {
        val jsonEncoder = encoder as JsonEncoder

        val jsonObject = buildJsonObject {
            put("application_name", value.applicationName)
            put("application_type", value.applicationType.name)
            put("server", Json.encodeToJsonElement(
                NullableServerSerializer,
                value.server
            ))
            put("address", Json.encodeToJsonElement(
                InetSocketAddressSerializer,
                value.address
            ))
            put("online_since", value.onlineSince)
            put("heap_size", value.heapSize)
            put("heap_max_size", value.heapMaxSize)
            put("heap_free_size", value.heapFreeSize)
            put("online_players", value.onlinePlayers)
        }

        jsonEncoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(
        decoder: Decoder
    ): ApplicationStatus {
        val jsonDecoder = decoder as JsonDecoder

        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        return ApplicationStatus(
            jsonObject["application_name"]!!.toString(),
            ApplicationType.valueOf(
                jsonObject["application_type"]!!.toString(),
            ),
            Json.decodeFromJsonElement(
                NullableServerSerializer,
                jsonObject["server"]!!
            ),
            Json.decodeFromJsonElement(
                InetSocketAddressSerializer,
                jsonObject["address"]!!
            ),
            jsonObject["online_since"]!!.jsonPrimitive.long
        ).apply {
            heapSize = jsonObject["heap_size"]!!.jsonPrimitive.long
            heapMaxSize = jsonObject["heap_max_size"]!!.jsonPrimitive.long
            heapFreeSize = jsonObject["heap_free_size"]!!.jsonPrimitive.long
            onlinePlayers = jsonObject["online_players"]!!.jsonPrimitive.int
        }
    }
}

object InetSocketAddressSerializer : KSerializer<InetSocketAddress> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NonNullableInetSocketAddress", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: InetSocketAddress
    ) {
        val jsonEncoder = encoder as JsonEncoder

        val jsonObject = buildJsonObject {
            put("address", value.address.hostAddress)
            put("port", value.port)
        }

        jsonEncoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(
        decoder: Decoder
    ): InetSocketAddress {
        val jsonDecoder = decoder as JsonDecoder

        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        return InetSocketAddress(
            jsonObject["address"]!!.jsonPrimitive.toString(),
            jsonObject["port"]!!.jsonPrimitive.int
        )
    }
}

object ServerSerializer : KSerializer<Server> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NonNullableServer", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Server
    ) {
        encoder.apply { encodeString(value.name.value) }
    }

    override fun deserialize(
        decoder: Decoder
    ): Server = CoreProvider.Cache.Local.SERVERS.provide().fetchByName(decoder.decodeString())!!
}

object NullableServerSerializer : KSerializer<Server?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NullableServer", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Server?
    ) {
        value?.let {
            encoder.apply {
                encodeBoolean(true)
                encodeString(value.name.value)
            }
        } ?: encoder.apply { encodeBoolean(false) }
    }

    override fun deserialize(
        decoder: Decoder
    ): Server? {
        val valid = decoder.decodeBoolean()

        return if (valid) {
            CoreProvider.Cache.Local.SERVERS.provide().fetchByName(decoder.decodeString())
        } else null
    }
}

object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NonNullableUUID", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: UUID
    ) {
        encoder.apply { encodeString(value.toString()) }
    }

    override fun deserialize(
        decoder: Decoder
    ): UUID = UUID.fromString(decoder.decodeString())
}

object EntityIDSerializer : KSerializer<EntityID<*>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NonNullableEntityID", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: EntityID<*>
    ) {
        val _value = value.value

        encoder.apply {
            when (_value) {
                is Int -> {
                    encodeString(Int::class.qualifiedName!!)
                    encodeInt(_value)
                    encodeString(value.table::class.qualifiedName!!)
                }
                is Double -> {
                    encodeString(Double::class.qualifiedName!!)
                    encodeDouble(_value)
                    encodeString(value.table::class.qualifiedName!!)
                }
                is Float -> {
                    encodeString(Float::class.qualifiedName!!)
                    encodeFloat(_value)
                    encodeString(value.table::class.qualifiedName!!)
                }
                is Byte -> {
                    encodeString(Byte::class.qualifiedName!!)
                    encodeByte(_value)
                    encodeString(value.table::class.qualifiedName!!)
                }
                is Short -> {
                    encodeString(Short::class.qualifiedName!!)
                    encodeShort(_value)
                    encodeString(value.table::class.qualifiedName!!)
                }
                is Long -> {
                    encodeString(Long::class.qualifiedName!!)
                    encodeLong(_value)
                    encodeString(value.table::class.qualifiedName!!)
                }
                is Char -> {
                    encodeString(Char::class.qualifiedName!!)
                    encodeChar(_value)
                    encodeString(value.table::class.qualifiedName!!)
                }
                is String -> {
                    encodeString(String::class.qualifiedName!!)
                    encodeString(_value)
                    encodeString(value.table::class.qualifiedName!!)
                }
                is UUID -> {
                    encodeString(UUID::class.qualifiedName!!)
                    encodeString(_value.toString())
                    encodeString(value.table::class.qualifiedName!!)
                }
            }
        }
    }

    override fun deserialize(
        decoder: Decoder
    ): EntityID<*> = when (decoder.decodeString()) {
        Int::class.qualifiedName -> {
            val value = decoder.decodeInt()
            val tableClassQualifiedName = decoder.decodeString()

            val idTable = Class.forName(tableClassQualifiedName).kotlin.objectInstance as IdTable<Int>

            EntityID(value, idTable)
        }
        Double::class.qualifiedName -> {
            val value = decoder.decodeDouble()
            val tableClassQualifiedName = decoder.decodeString()

            val idTable = Class.forName(tableClassQualifiedName).kotlin.objectInstance as IdTable<Double>

            EntityID(value, idTable)
        }
        Float::class.qualifiedName -> {
            val value = decoder.decodeFloat()
            val tableClassQualifiedName = decoder.decodeString()

            val idTable = Class.forName(tableClassQualifiedName).kotlin.objectInstance as IdTable<Float>

            EntityID(value, idTable)
        }
        Long::class.qualifiedName -> {
            val value = decoder.decodeLong()
            val tableClassQualifiedName = decoder.decodeString()

            val idTable = Class.forName(tableClassQualifiedName).kotlin.objectInstance as IdTable<Long>

            EntityID(value, idTable)
        }
        Short::class.qualifiedName -> {
            val value = decoder.decodeShort()
            val tableClassQualifiedName = decoder.decodeString()

            val idTable = Class.forName(tableClassQualifiedName).kotlin.objectInstance as IdTable<Short>

            EntityID(value, idTable)
        }
        Char::class.qualifiedName -> {
            val value = decoder.decodeChar()
            val tableClassQualifiedName = decoder.decodeString()

            val idTable = Class.forName(tableClassQualifiedName).kotlin.objectInstance as IdTable<Char>

            EntityID(value, idTable)
        }
        String::class.qualifiedName -> {
            val value = decoder.decodeString()
            val tableClassQualifiedName = decoder.decodeString()

            val idTable = Class.forName(tableClassQualifiedName).kotlin.objectInstance as IdTable<String>

            EntityID(value, idTable)
        }
        UUID::class.qualifiedName -> {
            val value = UUID.fromString(decoder.decodeString())
            val tableClassQualifiedName = decoder.decodeString()

            val idTable = Class.forName(tableClassQualifiedName).kotlin.objectInstance as IdTable<UUID>

            EntityID(value, idTable)
        }
        else -> error("Cannot decode")
    }
}

object DateTimeSerializer : KSerializer<DateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NonNullableDateTime", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: DateTime
    ) {
        encoder.apply { value.toString() }
    }

    override fun deserialize(
        decoder: Decoder
    ): DateTime = DateTime.parse(decoder.decodeString())
}