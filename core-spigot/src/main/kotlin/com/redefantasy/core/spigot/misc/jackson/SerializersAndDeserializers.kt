package com.redefantasy.core.spigot.misc.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer
import com.redefantasy.core.shared.CoreConstants
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
open class ItemStackSerializer : StdScalarSerializer<ItemStack>(
	ItemStack::class.java
) {

	override fun serialize(
		itemStack: ItemStack?,
		jsonGenerator: JsonGenerator,
		serializerProvider: SerializerProvider
	) {
		if (itemStack === null) {
			jsonGenerator.writeNull()
		} else {
			jsonGenerator.writeString(
				CoreConstants.JACKSON.writeValueAsString(
					itemStack.serialize()
				)
			)
		}
	}

	override fun serializeWithType(
		itemStack: ItemStack?,
		jsonGenerator: JsonGenerator,
		serializerProvider: SerializerProvider,
		typeSerializer: TypeSerializer
	) {
		val typeIdDef = typeSerializer.writeTypePrefix(
			jsonGenerator,
			typeSerializer.typeId(
				itemStack,
				ItemStack::class.java,
				JsonToken.VALUE_STRING
			)
		)

		this.serialize(itemStack, jsonGenerator, serializerProvider)

		typeSerializer.writeTypeSuffix(jsonGenerator, typeIdDef)
	}

}

open class ItemStackDeserializer : FromStringDeserializer<ItemStack>(
	ItemStack::class.java
) {

	override fun _deserialize(
		serializedItemStack: String?,
		deserializationContext: DeserializationContext
	): ItemStack? {
		println("[JACKSON]: Deserialize")

		return if (serializedItemStack !== null) ItemStack.deserialize(
			CoreConstants.JACKSON.readValue(
				serializedItemStack,
				MutableMap::class.java
			) as MutableMap<String, Any?>
		) else null
	}

}