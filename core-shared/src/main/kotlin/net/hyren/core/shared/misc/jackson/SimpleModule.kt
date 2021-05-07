package net.hyren.core.shared.misc.jackson

import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer

/**
 * @author Gutyerrez
 */
inline fun <reified T> SimpleModule.addSerializerAndDeserializer(
	serializer: StdScalarSerializer<T>,
	deserializer: StdScalarDeserializer<T>
) {
	this.addSerializer(T::class.java, serializer)
	this.addDeserializer(T::class.java, deserializer)
}