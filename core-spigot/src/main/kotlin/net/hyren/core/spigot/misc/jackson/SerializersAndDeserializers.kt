package net.hyren.core.spigot.misc.jackson

import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
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
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NonNullableUUID", PrimitiveKind.STRING)

	override fun serialize(
		encoder: Encoder,
		value: ItemStack
	) {
		encoder.apply {
			encodeString(
				Json.encodeToString(
					value.serialize()
				)
			)
		}
	}

	override fun deserialize(
		decoder: Decoder
	): ItemStack = ItemStack.deserialize(Json.decodeFromString(decoder.decodeString()))
}