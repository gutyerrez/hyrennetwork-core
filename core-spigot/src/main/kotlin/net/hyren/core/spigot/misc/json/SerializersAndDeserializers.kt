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
	): ItemStack = ItemStack.deserialize(Json.decodeFromJsonElement((decoder as JsonDecoder).decodeJsonElement()))
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