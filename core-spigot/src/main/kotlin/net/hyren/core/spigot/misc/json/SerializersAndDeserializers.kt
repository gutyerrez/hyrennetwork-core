package net.hyren.core.spigot.misc.json

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
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
	) {
		val formatter = Json { allowStructuredMapKeys = true }

		encoder.encodeString(formatter.encodeToString(value.serialize()))
	}

	override fun deserialize(
		decoder: Decoder
	): ItemStack = ItemStack.deserialize(Json.decodeFromString(decoder.decodeString()))
}