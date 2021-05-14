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

		serializedItemStack.entries.forEach { entry ->
			when (entry.value) {
				is JsonObject -> {
					(entry.value as JsonObject).entries.forEach {
						val anotherMap = mutableMapOf<String, Any>()

						when {
							entry.value is JsonPrimitive -> {
								when {
									(it.value as JsonPrimitive).intOrNull != null -> {
										anotherMap[it.key] = (it.value as JsonPrimitive).int
									}
									(it.value as JsonPrimitive).doubleOrNull != null -> {
										anotherMap[it.key] = (it.value as JsonPrimitive).double
									}
									(it.value as JsonPrimitive).floatOrNull != null -> {
										anotherMap[it.key] = (it.value as JsonPrimitive).float
									}
									(it.value as JsonPrimitive).longOrNull != null -> {
										anotherMap[it.key] = (it.value as JsonPrimitive).long
									}
									(it.value as JsonPrimitive).booleanOrNull != null -> {
										anotherMap[it.key] = (it.value as JsonPrimitive).boolean
									}
									(it.value as JsonPrimitive).isString -> {
										anotherMap[it.key] = (it.value as JsonPrimitive).toString()
									}
								}
							}
							entry.value is JsonArray -> {
								val jsonArray = entry.value.jsonArray

								val lore = mutableListOf<String>()

								jsonArray.forEach { element ->
									lore.add(element.toString())
								}
							}
						}
					}
				}
				is JsonPrimitive -> {
					when {
						(entry.value as JsonPrimitive).intOrNull != null -> {
							deserializedValues[entry.key] = (entry.value as JsonPrimitive).int
						}
						(entry.value as JsonPrimitive).doubleOrNull != null -> {
							deserializedValues[entry.key] = (entry.value as JsonPrimitive).double
						}
						(entry.value as JsonPrimitive).floatOrNull != null -> {
							deserializedValues[entry.key] = (entry.value as JsonPrimitive).float
						}
						(entry.value as JsonPrimitive).longOrNull != null -> {
							deserializedValues[entry.key] = (entry.value as JsonPrimitive).long
						}
						(entry.value as JsonPrimitive).booleanOrNull != null -> {
							deserializedValues[entry.key] = (entry.value as JsonPrimitive).boolean
						}
						(entry.value as JsonPrimitive).isString -> {
							deserializedValues[entry.key] = (entry.value as JsonPrimitive).toString()
						}
					}
				}
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