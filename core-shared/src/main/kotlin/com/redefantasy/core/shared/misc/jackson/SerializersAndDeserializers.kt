package com.redefantasy.core.shared.misc.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
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

internal class ServerDeserializer : FromStringDeserializer<Server>(
    Server::class.java
) {

    override fun _deserialize(
        serverName: String,
        deserializationContext: DeserializationContext
    ) = CoreProvider.Cache.Local.SERVERS.provide().fetchByName(serverName)

}

internal class ApplicationSerializer : StdScalarSerializer<Application>(
    Application::class.java
) {

    override fun serialize(
        server: Application,
        jsonGenerator: JsonGenerator,
        serializerProvider: SerializerProvider
    ) {
        jsonGenerator.writeStringField("name", server.name)
    }

    override fun serializeWithType(
        value: Application,
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

internal class ApplicationDeserializer : FromStringDeserializer<Application>(
    Application::class.java
) {

    override fun _deserialize(
        applicationName: String,
        deserializationContext: DeserializationContext
    ) = CoreProvider.Cache.Local.APPLICATIONS.provide().fetchByName(applicationName)

}
