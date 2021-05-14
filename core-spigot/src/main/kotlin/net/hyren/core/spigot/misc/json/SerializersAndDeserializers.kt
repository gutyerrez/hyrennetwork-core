package net.hyren.core.spigot.misc.json

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import net.hyren.core.spigot.misc.server.configuration.settings.ServerSettings
import org.bukkit.inventory.ItemStack
import kotlin.reflect.full.starProjectedType

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
			val _key = it.key
			val _value = it.value

			val element = Json.encodeToJsonElement(serializer(_value::class.starProjectedType), _value)

			deserializedValues[_key] = element
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