package com.redefantasy.core.shared.misc.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import java.io.IOException
import java.util.*

/**
 * @author Gutyerrez
 */
internal class EntityIDSerializer : StdScalarSerializer<EntityID<*>>(
    EntityID::class.java
) {

    override fun serialize(
        value: EntityID<*>,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider
    ) {
        jsonGenerator.writeObjectField("table_name", value.table::class.java)
        jsonGenerator.writeObjectField("value", value.value)
    }

    override fun serializeWithType(
        value: EntityID<*>,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider,
        typeSerializer: TypeSerializer
    ) {
        val typeIdDef = typeSerializer.writeTypePrefix(
            jsonGenerator,
            typeSerializer.typeId(
                value,
                EntityID::class.java,
                JsonToken.VALUE_EMBEDDED_OBJECT
            )
        )

        this.serialize(value, jsonGenerator, serializerProvider)

        typeSerializer.writeTypeSuffix(jsonGenerator, typeIdDef)
    }

}

internal class EntityIDDeserializer : StdDeserializer<EntityID<*>>(
    EntityID::class.java
) {

    override fun deserialize(
        jsonParser: JsonParser,
        deserializationContext: DeserializationContext
    ): EntityID<*>? {
        val node = jsonParser.readValueAsTree<JsonNode>()

        val tableClassName = node.get("table_name")

        if (tableClassName === null) throw IOException("Invalid table name")

        val table = Class.forName(tableClassName.textValue())
        val value = node.get("value")

        if (table !is IdTable<*>) throw IOException("Invalid table")

        return when {
            value.isObject -> {
                try {
                    val uuid = UUID.fromString(value.asText())

                    EntityID(
                        uuid,
                        table as IdTable<UUID>
                    )
                } catch (ignored: Exception) { return null }
            }
            value.isTextual -> {
                EntityID(
                    value.textValue(),
                    table as IdTable<String>
                )
            }
            value.isInt -> {
                EntityID(
                    value.intValue(),
                    table as IdTable<Int>
                )
            }
            value.isLong -> {
                EntityID(
                    value.longValue(),
                    table as IdTable<Long>
                )
            }
            else -> throw IOException("Cannot find a codec")
        }
    }

}