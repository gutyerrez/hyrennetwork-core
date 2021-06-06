package net.hyren.core.shared.echo.api.buffer

import kotlinx.serialization.json.JsonElement
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.misc.json.*
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.world.location.SerializedLocation
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.io.*
import java.net.*
import java.util.*

/**
 * @author SrGutyerrez
 **/
class EchoBufferOutput {

    private val _bytearrayOutputStream = ByteArrayOutputStream()
    private val buffer = DataOutputStream(_bytearrayOutputStream)

    fun writeBoolean(boolean: Boolean) = buffer.writeBoolean(boolean)

    fun writeByte(byte: Int) = buffer.writeByte(byte)

    fun writeShort(short: Int) = buffer.writeShort(short)

    fun writeChar(char: Int) = buffer.writeChar(char)

    fun writeInt(int: Int?) {
        if (int == null) {
            writeBoolean(false)
        } else {
            writeBoolean(true)
            buffer.writeInt(int)
        }
    }

    fun writeLong(long: Long) = buffer.writeLong(long)

    fun writeDouble(double: Double) = buffer.writeDouble(double)

    fun writeFloat(float: Float?) {
        if (float === null) {
            writeBoolean(false)
        } else {
            writeBoolean(true)
            buffer.writeFloat(float)
        }
    }

    fun writeString(string: String?) {
        if (string === null) {
            writeBoolean(false)
        } else {
            writeBoolean(true)
            buffer.writeUTF(string)
        }
    }

    fun <T : Enum<T>> writeEnum(enum: T?) = writeString(
        Optional.ofNullable(enum).map { it.name }.orElse(null)
    )

    fun writeUUID(uuid: UUID?) {
        if (uuid === null) {
            writeBoolean(false)
        } else {
            writeBoolean(true)
            writeLong(uuid.mostSignificantBits)
            writeLong(uuid.leastSignificantBits)
        }
    }

    inline fun <reified T: Comparable<T>> writeEntityID(entityId: EntityID<T>?) {
        if (entityId === null) {
            writeBoolean(false)
        } else {
            writeBoolean(true)
            writeString(
                KJson.encodeToString(entityId)
            )
        }
    }

    fun writeByteArray(byteArray: ByteArray) {
        writeBoolean(true)
        buffer.write(byteArray)
    }

    fun writeAddress(address: InetSocketAddress) {
        val _address = address.address

        var host = _address?.toString()?.trim() ?: address.hostName

        val index = host.indexOf('/')

        if (index >= 0) {
            if (index == 0) {
                host = if (_address is Inet6Address) {
                    String.format("[%s]", host.substring(1))
                } else host.substring(1)
            } else {
                host = host.substring(0, index)
            }
        }

        writeString("$host:${address.port}")
    }

    fun writeApplication(application: Application?) {
        if (application == null) {
            writeBoolean(false)
        } else {
            writeBoolean(true)
            writeString(application.name)
            writeString(application.displayName)
            writeInt(application.slots)
            writeAddress(application.address)
            writeEnum(application.applicationType)
            writeServer(application.server)
            writeEnum(application.restrictJoin)
        }
    }

    fun writeServer(server: Server?) {
        writeEntityID(server?.name)
    }

    fun writeSerializedLocation(serializedLocation: SerializedLocation) {
        writeString(serializedLocation.toString())
    }

    fun writeJsonObject(jsonObject: JsonElement) = writeString(jsonObject.asString())

    fun writeBaseComponent(baseComponents: Array<BaseComponent>?) {
        if (baseComponents === null) {
            writeString(null)
        } else {
            val serialized = ComponentSerializer.toString(*baseComponents)

            writeString(serialized)
        }
    }

    fun writeBaseComponent(baseComponent: BaseComponent?) {
        if (baseComponent === null) {
            writeBoolean(false)
        } else {
            val serialized = ComponentSerializer.toString(baseComponent)

            writeBoolean(true)
            writeString(serialized)
        }
    }

    fun writeDateTime(dateTime: DateTime?) {
        if (dateTime == null) {
            writeBoolean(false)
        } else {
            writeBoolean(true)
            writeString(KJson.encodeToString(DateTime::class, dateTime))
        }
    }

    inline fun <reified T> writeList(list: List<T>?) {
        if (list === null) {
            writeBoolean(false)
        } else {
            writeBoolean(true)
            writeString(
                KJson.encodeToString(list)
            )
        }
    }

    inline fun <reified T> writeArray(array: Array<T>?) {
        if (array === null) {
            writeBoolean(false)
        } else {
            writeBoolean(true)
            writeString(
                KJson.encodeToString(array)
            )
        }
    }

    fun toByteArray() = _bytearrayOutputStream.toByteArray()

}