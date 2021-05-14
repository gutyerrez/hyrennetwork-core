package net.hyren.core.spigot.misc.json

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import net.hyren.core.spigot.misc.server.configuration.settings.ServerSettings
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
object ItemStackSerializer : SerializationStrategy<ItemStack>, DeserializationStrategy<ItemStack> {
	override val descriptor: SerialDescriptor = ContextualSerializer(
		ItemStack::class,
		null,
		emptyArray()
	).descriptor

	override fun serialize(
		encoder: Encoder,
		value: ItemStack
	) {
		encoder.encodeString(Json.encodeToString(value.serialize()))
	}

	override fun deserialize(
		decoder: Decoder
	): ItemStack {
		val value = decoder.decodeString()

		println("To deserialize: $value")

		return ItemStack.deserialize(Json.decodeFromString(value))
	}
}

object ServerSettingsSerializer : SerializationStrategy<ServerSettings>, DeserializationStrategy<ServerSettings> {
	override val descriptor: SerialDescriptor = ContextualSerializer(
		ServerSettings::class,
		null,
		emptyArray()
	).descriptor

	override fun serialize(
		encoder: Encoder,
		value: ServerSettings
	) {
		encoder.encodeString(
			Json.encodeToString(value)
		)
	}

	override fun deserialize(
		decoder: Decoder
	): ServerSettings = Json.decodeFromString(decoder.decodeString())
}