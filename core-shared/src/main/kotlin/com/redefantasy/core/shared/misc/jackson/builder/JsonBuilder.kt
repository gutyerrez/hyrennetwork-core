package com.redefantasy.core.shared.misc.jackson.builder

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.redefantasy.core.shared.CoreConstants
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*

/**
 * @author Gutyerrez
 */
class JsonBuilder {

	val JSON_NODE = CoreConstants.JACKSON.createObjectNode()

	constructor()

	constructor(
		key: String,
		value: String
	) {
		append(key, value)
	}

	fun append(
		key: String,
		value: Int?
	) = apply {
		JSON_NODE.put(key, value)
	}

	fun append(
		key: String,
		value: String?
	) = apply {
		JSON_NODE.put(key, value)
	}

	fun append(
		key: String,
		value: Double?
	) = apply {
		JSON_NODE.put(key, value)
	}

	fun append(
		key: String,
		value: Float?
	) = apply {
		JSON_NODE.put(key, value)
	}

	fun append(
		key: String,
		value: Long?
	) = apply {
		JSON_NODE.put(key, value)
	}

	fun append(
		key: String,
		value: Short?
	) = apply {
		JSON_NODE.put(key, value)
	}

	fun append(
		key: String,
		value: Boolean?
	) = apply {
		JSON_NODE.put(key, value)
	}

	fun append(
		key: String,
		value: ByteArray?
	) = apply {
		JSON_NODE.put(key, value)
	}

	fun append(
		key: String,
		value: DateTime?
	) = apply {
		JSON_NODE.put(key, value?.toString())
	}

	fun append(
		key: String,
		value: UUID?
	) = apply {
		JSON_NODE.put(key, value?.toString())
	}

	fun append(
		key: String,
		value: JsonNode?
	) = apply {
		JSON_NODE.replace(key, value)
	}

	inline fun <reified T : Comparable<T>> append(
		key: String,
		value: T?
	) = apply {
		JSON_NODE.put(
			key, CoreConstants.JACKSON.writeValueAsString(
				value
			)
		)
	}

	inline fun <reified T : Comparable<T>> append(
		key: String,
		value: EntityID<T>?
	) = apply {
		JSON_NODE.put(key, value?.value?.toString())
	}

	inline fun <reified T> append(
		key: String,
		value: Array<T>?
	) = apply {
		value?.forEach {
			JSON_NODE.withArray(key).add(
				CoreConstants.JACKSON.writeValueAsString(
					it
				)
			)
		}
	}

	inline fun <reified T> append(
		key: String,
		value: List<T>?
	) = apply {
		value?.forEach {
			JSON_NODE.withArray(key).add(
				CoreConstants.JACKSON.writeValueAsString(
					it
				)
			)
		}
	}

	inline fun <reified K, reified V> append(
		key: String,
		value: Map<K, V>?
	) = apply {
		value?.forEach {
			JSON_NODE.withArray(key).add(
				CoreConstants.JACKSON.writeValueAsString(
					Pair(
						it.key,
						it.value
					)
				)
			)
		}
	}

	fun build(): ObjectNode = JSON_NODE

}