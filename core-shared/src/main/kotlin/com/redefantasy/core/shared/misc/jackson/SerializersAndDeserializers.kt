package com.redefantasy.core.shared.misc.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.servers.data.Server

/**
 * @author Gutyerrez
 */
open class ServerSerializer : StdScalarSerializer<Server>(
    Server::class.java
) {

    override fun serialize(
        server: Server,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider
    ) {
        jsonGenerator.writeStringField("name", server.getName())
    }

    override fun serializeWithType(
        server: Server,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider,
        typeSerializer: TypeSerializer
    ) {
        val typeIdDef = typeSerializer.writeTypePrefix(
            jsonGenerator,
            typeSerializer.typeId(
                server,
                Server::class.java,
                JsonToken.VALUE_STRING
            )
        )

        this.serialize(server, jsonGenerator, serializerProvider)

        typeSerializer.writeTypeSuffix(jsonGenerator, typeIdDef)
    }

}

open class ServerDeserializer : FromStringDeserializer<Server>(
    Server::class.java
) {

    override fun _deserialize(
        serverName: String,
        deserializationContext: DeserializationContext
    ) = CoreProvider.Cache.Local.SERVERS.provide().fetchByName(serverName)

}
