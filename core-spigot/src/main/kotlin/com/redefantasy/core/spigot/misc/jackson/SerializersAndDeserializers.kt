package com.redefantasy.core.spigot.misc.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.redefantasy.core.shared.misc.jackson.builder.JsonBuilder
import net.minecraft.server.v1_8_R3.NBTBase
import net.minecraft.server.v1_8_R3.NBTTagCompound
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

/**
 * @author Gutyerrez
 */
open class ItemStackSerializer : StdSerializer<ItemStack>(
	ItemStack::class.java
) {

	init {
		println("DALEEEe")
	}

	val CRAFT_META_ITEM_CLASS = Class.forName(
		"org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaItem"
	)

	override fun serialize(
		itemStack: ItemStack?,
		jsonGenerator: JsonGenerator,
		serializerProvider: SerializerProvider
	) {
		if (itemStack === null) {
			jsonGenerator.writeNull()
		} else {
			println("Tem item meta? ${itemStack.hasItemMeta()}")

			val craftItemStack = CraftItemStack.asNMSCopy(itemStack)

			val amount = itemStack.amount
			val durability = itemStack.durability
			val itemMeta = if (itemStack.hasItemMeta()) itemStack.itemMeta else null
			val materialData = itemStack.data
			val tags = craftItemStack.tag

			val mapField = NBTTagCompound::class.java.getDeclaredField("map")

			mapField.isAccessible = true

			val map = mapField.get(tags) as? Map<String, NBTBase>

			val serializedItemMeta = itemMeta?.let {
				when (it::class.java::isAssignableFrom) {
					CRAFT_META_ITEM_CLASS -> JsonBuilder().append(
						"display_name", itemMeta.displayName
					).append(
						"lore", itemMeta.lore
					).append(
						"enchants", itemMeta.enchants
					).append(
						"nbt_tags", map
					).build()
					else -> throw ClassCastException("Item meta has not assignable from none of allowed serializers")
				}
			}

			jsonGenerator.writeString(
				JsonBuilder().append(
					"amount", amount
				).append(
					"durability", durability
				).append(
					"material_data", JsonBuilder().append(
						"type", materialData.itemType.id
					).append(
						"data", materialData.data
					).build()
				).append(
					"item_meta", serializedItemMeta
				).build().toPrettyString()
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