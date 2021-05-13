package net.hyren.core.shared.misc.exposed

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.vendors.MysqlDialect
import org.jetbrains.exposed.sql.vendors.currentDialect
import java.nio.ByteBuffer
import java.util.*

/**
 * @author Gutyerrez
 */
fun Table.uuid4(columnName: String): Column<UUID> = registerColumn(columnName, UUIDColumnType())

class UUIDColumnType : ColumnType() {

    override fun sqlType(): String = currentDialect.dataTypeProvider.uuidType()

    override fun valueFromDB(value: Any): UUID = when {
        value is UUID -> value
        value is ByteArray -> {
            println("ByteArray: " + value.contentToString())
            val possibleUUID = String(value)

            if (possibleUUID.matches(uuidRegexp)) {
                UUID.fromString(possibleUUID)
            } else {
                ByteBuffer.wrap(value).let { b -> UUID(b.long, b.long) }
            }
        }
        value is String && value.matches(uuidRegexp) -> UUID.fromString(value)
        value is String -> ByteBuffer.wrap(value.toByteArray()).let { b -> UUID(b.long, b.long) }
        else -> error("Unexpected value of type UUID: $value of ${value::class.qualifiedName}")
    }

    override fun notNullValueToDB(value: Any): Any = when (currentDialect) {
        is MysqlDialect -> {
            value as UUID

            val byteArray = ByteBuffer.allocate(36).putLong(
                value.mostSignificantBits
            ).putLong(
                value.leastSignificantBits
            ).array()

            println("To DB: ${ByteBuffer.wrap(byteArray).let { b -> UUID(b.long, b.long) }}")

            byteArray
        }
        else -> {
            println(currentDialect::class.qualifiedName)

            currentDialect.dataTypeProvider.uuidToDB(valueToUUID(value))
        }
    }

    override fun nonNullValueToString(value: Any): String = "'${valueToUUID(value)}'"

    private fun valueToUUID(value: Any): UUID = when (value) {
        is UUID -> {
            println("UUID is: " + value)

            value
        }
        is String -> UUID.fromString(value)
        is ByteArray -> {
            println("Is that")
            val possibleUUID = String(value)

            if (possibleUUID.matches(uuidRegexp)) {
                UUID.fromString(possibleUUID)
            } else {
                ByteBuffer.wrap(value).let { b -> UUID(b.long, b.long) }
            }
        }
        else -> error("Unexpected value of type UUID: ${value.javaClass.canonicalName}")
    }

    companion object {
        private val uuidRegexp = Regex("[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}", RegexOption.IGNORE_CASE)
    }

}