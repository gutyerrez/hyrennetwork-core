package net.hyren.core.spigot.misc.json

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import net.hyren.core.spigot.misc.server.configuration.settings.ServerSettings
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
object ItemStackSerializer : KSerializer<ItemStack> {
	override val descriptor: SerialDescriptor = ContextualSerializer(
		ItemStack::class,
		null,
		emptyArray()
	).descriptor

	override fun serialize(
		encoder: Encoder,
		value: ItemStack
	) = (encoder as JsonEncoder).encodeJsonElement(Json.encodeToJsonElement(value))

	override fun deserialize(
		decoder: Decoder
	): ItemStack {
		lateinit var itemStack: ItemStack

		val serializedItemStack = (decoder as JsonDecoder).decodeJsonElement().jsonObject

		val deserializedValues = mutableMapOf<String, Any>()

		serializedItemStack.entries.forEach {
			if (it.value !is JsonPrimitive) return@forEach

			val _key = it.key
			val _value = it.value.jsonPrimitive

			when {
				_value.intOrNull != null -> deserializedValues[_key] = _value.int
				_value.doubleOrNull != null -> deserializedValues[_key] = _value.double
				_value.floatOrNull != null -> deserializedValues[_key] = _value.float
				_value.longOrNull != null -> deserializedValues[_key] = _value.long
				_value.booleanOrNull != null -> deserializedValues[_key] = _value.boolean
				_value.isString -> deserializedValues[_key] = _value.toString()
			}
		}

		println(deserializedValues)

		itemStack = ItemStack.deserialize(deserializedValues)

		return itemStack
	}
}

object ServerSettingsSerializer : KSerializer<ServerSettings> {
	override val descriptor: SerialDescriptor = ContextualSerializer(
		ServerSettings::class,
		null,
		emptyArray()
	).descriptor

	override fun serialize(
		encoder: Encoder,
		value: ServerSettings
	) = (encoder as JsonEncoder).encodeJsonElement(Json.encodeToJsonElement(value))

	override fun deserialize(
		decoder: Decoder
	): ServerSettings = Json.decodeFromJsonElement((decoder as JsonDecoder).decodeJsonElement())
}