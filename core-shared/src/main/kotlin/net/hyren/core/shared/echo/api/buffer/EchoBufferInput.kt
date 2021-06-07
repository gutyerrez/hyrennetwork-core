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

    fun readInt(): Int? = if (readBoolean()) {
        buffer.readInt()
    } else { null }

    fun readLong() = buffer.readLong()

    fun readDouble() = buffer.readDouble()

    fun readFloat(): Float? = if (readBoolean()) {
        buffer.readFloat()
    } else { null }

    fun readString(): String? = if (readBoolean()) {
        buffer.readUTF()
    } else { null }

    inline fun <reified T : Enum<T>> readEnum(
        kClass: KClass<T>,
        deft: T? = null
    ): T? = readString().run {
        EnumSet.allOf(kClass.java).firstOrNull { it.name == this } ?: deft
    }

    fun readUUID(): UUID? = if (buffer.readBoolean()) {
        UUID(readLong(), readLong())
    } else { null }

    @Deprecated(
        "readEntityID(table: IdTable<T>) is deprecated",
        ReplaceWith("readEntityID()"),
        DeprecationLevel.WARNING
    )
    inline fun <reified T: Comparable<T>> readEntityID(
        table: IdTable<T> // ignore
    ): EntityID<T>? = readEntityID()

    inline fun <reified T: Comparable<T>> readEntityID(): EntityID<T>? = if (readBoolean()) {
        KJson.decodeFromString(readString())
    } else { null }

    @Deprecated(
        "read address is deprecated",
        ReplaceWith("readAddressInetSocketAddress()"),
        DeprecationLevel.WARNING
    )
    fun readAddress() = readAddressInetSocketAddress()

    fun readAddressInetSocketAddress(): InetSocketAddress? = readString()?.let {
        if (it.startsWith("[")) {
            val i = it.lastIndexOf(']')

            if (i == -1) { return null }

            val j = it.indexOf(':', i)
            val port = if (j > -1) it.substring(j + 1).toInt() else 0

            InetSocketAddress(it.substring(0, i + 1), port)
        } else {
            val i = it.indexOf(':')

            if (i != -1 && it.indexOf(':', i + 1) == -1) {
                val port = it.substring(i + 1).toInt()

                InetSocketAddress(it.substring(0, i), port)
            } else {
                InetSocketAddress(it, 0)
            }
        }
    }

    fun readApplication(): Application? = if (readBoolean()) {
        Application(
            readString()!!,
            readString()!!,
            readInt(),
            readAddressInetSocketAddress()!!,
            readEnum(ApplicationType::class)!!,
            readServer(),
            readEnum(Group::class)
        )
    } else { null }

    fun readServer() = CoreProvider.Cache.Local.SERVERS.provide().fetchByName(readEntityID())

    fun readSerializedLocation() = SerializedLocation.of(readString())

    fun readBaseComponent() = ComponentSerializer.parse(readString())

    fun readDateTime(): DateTime? = if (readBoolean()) {
        KJson.decodeFromString(
            DateTime::class,
            readString()
        ) as DateTime
    } else { null }

    inline fun <reified T> readList(): List<T>? = if (readBoolean()) {
        KJson.decodeFromString(readString())
    } else { null }

    inline fun <reified T> readArray(): Array<T>? = if (readBoolean()) {
        KJson.decodeFromString(readString())
    } else { null }

    fun readJson() = KJson.encodeToJsonElement(readString())

}