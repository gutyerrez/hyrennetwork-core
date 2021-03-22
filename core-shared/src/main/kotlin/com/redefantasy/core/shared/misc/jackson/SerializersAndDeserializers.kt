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
import com.redefantasy.core.shared.servers.data.Server

/**
 * @author Gutyerrez
 */
internal class ServerSerializer : StdScalarSerializer<Server>(
    Server::class.java
) {

    override fun serialize(
        server: Server,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider
    ) {
        jsonGenerator.writeStringField("name", server.getName())
        jsonGenerator.writeStringField("display_name", server.displayName)

        println("Serializar => $server")
    }

    override fun serializeWithType(
        value: Server,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider,
        typeSerializer: TypeSerializer
    ) {
        val typeIdDef = typeSerializer.writeTypePrefix(
            jsonGenerator,
            typeSerializer.typeId(
                value,
                Server::class.java,
                JsonToken.VALUE_EMBEDDED_OBJECT
            )
        )

        this.serialize(value, jsonGenerator, serializerProvider)

        typeSerializer.writeTypeSuffix(jsonGenerator, typeIdDef)
    }

}

internal class ServerDeserializer : StdDeserializer<Server>(
    Server::class.java
) {

    override fun deserialize(
        jsonParser: JsonParser,
        deserializationContext: DeserializationContext
    ): Server? {
        val node = jsonParser.readValueAsTree<JsonNode>()

        println("Deserializar: $node")

        TODO("não implementado")
    }

}