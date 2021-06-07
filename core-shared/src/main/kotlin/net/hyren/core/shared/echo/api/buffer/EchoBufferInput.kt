package net.hyren.core.shared.echo.api.buffer

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.misc.json.KJson
import net.hyren.core.shared.world.location.SerializedLocation
import net.md_5.bungee.chat.ComponentSerializer
import org.jetbrains.exposed.dao.id.*
import org.joda.time.DateTime
import java.io.*
import java.net.InetSocketAddress
import java.util.*
import kotlin.reflect.KClass

/**
 * @author SrGutyerrez
 **/
class EchoBufferInput(
    bytes: ByteArray
) {

    private val buffer = DataInputStream(ByteArrayInputStream(bytes))

    fun readBoolean() = buffer.readBoolean()

    fun readByte() = buffer.readByte()

    fun readUnsignedByte() = buffer.readUnsignedByte()

    fun readShort() = buffer.readShort()

    fun readUnsignedShort() = buffer.readUnsignedShort()

    fun readChar() = buffer.readChar()

    fun readInt(): Int? {
        val valid = readBoolean()

        if (valid) {
            return buffer.readInt()
        }

        return null
    }

    fun readLong() = buffer.readLong()

    fun readDouble() = buffer.readDouble()

    fun readFloat(): Float? {
        val valid = readBoolean()

        if (valid) {
            return buffer.readFloat()
        }

        return null
    }

    fun readString(): String? {
        val valid = readBoolean()

        println("Is valid: $valid")

        if (valid) {
            val utf = buffer.readUTF()

            println("Retornar o valor válido: $utf")

            return utf
        }

        return null
    }

    inline fun <reified T : Enum<T>> readEnum(
        kClass: KClass<T>,
        deft: T? = null
    ): T? {
        return readString().run {
            EnumSet.allOf(kClass.java).firstOrNull { enum -> enum.name == this } ?: deft
        }
    }

    fun readUUID(): UUID? {
        val valid = buffer.readBoolean()

        if (valid) {
            val mostSignificantBits = buffer.readLong()
            val leastSignificantBits = buffer.readLong()

            return UUID(mostSignificantBits, leastSignificantBits)
        }

        return null
    }

    @Deprecated(
        "readEntityID(table: IdTable<T>) is deprecated",
        ReplaceWith("readEntityID()"),
        DeprecationLevel.WARNING
    )
    inline fun <reified T: Comparable<T>> readEntityID(
        table: IdTable<T> // ignore
    ): EntityID<T>? {
        val valid = readBoolean()

        if (valid) {
            return KJson.decodeFromString(readString())
        }

        return null
    }

    inline fun <reified T: Comparable<T>> readEntityID(): EntityID<T>? {
        val valid = readBoolean()

        if (valid) {
            return KJson.decodeFromString(readString()!!)
        }

        return null
    }

    @Deprecated(
        "read address is deprecated",
        ReplaceWith("readAddressInetSocketAddress()"),
        DeprecationLevel.WARNING
    )
    fun readAddress() = readAddressInetSocketAddress()

    fun readAddressInetSocketAddress(): InetSocketAddress? {
        val value = readString() ?: return null

        if (value.startsWith("[")) {
            val i = value.lastIndexOf(']')

            if (i == -1) {
                return null
            }

            val j = value.indexOf(':', i)
            val port = if (j > -1) value.substring(j + 1).toInt() else 0

            return InetSocketAddress(value.substring(0, i + 1), port)
        } else {
            val i = value.indexOf(':')

            return if (i != -1 && value.indexOf(':', i + 1) == -1) {
                val port = value.substring(i + 1).toInt()

                InetSocketAddress(value.substring(0, i), port)
            } else {
                InetSocketAddress(value, 0)
            }
        }
    }

    fun readApplication(): Application? {
        val valid = readBoolean()

        if (valid) return Application(
            readString()!!,
            readString()!!,
            readInt(),
            readAddressInetSocketAddress()!!,
            readEnum(ApplicationType::class)!!,
            readServer(),
            readEnum(Group::class)
        )

        return null
    }

    fun readServer() = CoreProvider.Cache.Local.SERVERS.provide().fetchByName(readEntityID())

    fun readSerializedLocation() = SerializedLocation.of(readString())

    fun readBaseComponent() = ComponentSerializer.parse(readString())

    fun readDateTime(): DateTime? = if (readBoolean()) {
        KJson.decodeFromString(
            DateTime::class,
            readString()
        ) as DateTime
    } else {
        null
    }

    inline fun <reified T> readList(): List<T>? {
        val valid = readBoolean()

        if (valid) {
            return KJson.decodeFromString(readString())
        }

        return null
    }

    inline fun <reified T> readArray(): Array<T>? {
        val valid = readBoolean()

        if (valid) {
            return KJson.decodeFromString(readString())
        }

        return null
    }

    fun readJson() = KJson.encodeToJsonElement(readString())

}