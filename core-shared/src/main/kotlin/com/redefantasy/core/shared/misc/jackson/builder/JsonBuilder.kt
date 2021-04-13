package com.redefantasy.core.shared.misc.jackson.builder

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
		JSON_NODE.put(
			key, value?.toString()
		)
	}

	fun append(
		key: String,
		value: UUID?
	) = apply {
		JSON_NODE.put(key, value?.toString())
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
		JSON_NODE.put(
			key, CoreConstants.JACKSON.writeValueAsString(
				value?.value
			)
		)
	}

	inline fun <reified T> append(
		key: String,
		value: Array<T>?
	) = apply {
		value?.forEach {
			JSON_NODE.putArray(key).add(
				CoreConstants.JACKSON.writeValueAsString(
					it
				)
			)
		}
	}

	fun build() = JSON_NODE

}