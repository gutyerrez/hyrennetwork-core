package net.hyren.core.shared.misc.kotlin

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.servers.data.Server
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.joda.time.DateTime
import java.net.InetSocketAddress
import java.util.*

/**
 * @author Gutyerrez
 */
object InetSocketAddressSerializer : KSerializer<InetSocketAddress> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NonNullableInetSocketAddress", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: InetSocketAddress
    ) {
        encoder.apply {
            encodeString(value.address.hostAddress)
            encodeInt(value.port)
        }
    }

    override fun deserialize(
        decoder: Decoder
    ) = InetSocketAddress(
        decoder.decodeString(),
        decoder.decodeInt()
    )
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

@InternalSerializationApi
@ExperimentalSerializationApi
object DynamicLookupSerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = ContextualSerializer(
        Any::class,
        null,
        emptyArray()
    ).descriptor

    override fun serialize(
        encoder: Encoder,
        value: Any
    ) {
        val actualSerializer = encoder.serializersModule.getContextual(value::class) ?: value::class.serializer()

        encoder.encodeSerializableValue(actualSerializer as KSerializer<Any>, value)
    }

    override fun deserialize(
        decoder: Decoder
    ) = error("Unsupported")
}