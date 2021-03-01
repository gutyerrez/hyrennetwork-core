package com.redefantasy.core.shared.echo.api.buffer

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import com.google.gson.JsonObject
import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.world.location.SerializedLocation
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer
import java.io.Serializable
import java.net.Inet6Address
import java.net.InetSocketAddress
import java.util.*

/**
 * @author SrGutyerrez
 **/
class EchoBufferOutput {

    private val buffer: ByteArrayDataOutput = ByteStreams.newDataOutput()

    fun writeBoolean(boolean: Boolean) = this.buffer.writeBoolean(boolean)

    fun writeByte(byte: Int) = this.buffer.writeByte(byte)

    fun writeShort(short: Int) = this.buffer.writeShort(short)

    fun writeChar(char: Int) = this.buffer.writeChar(char)

    fun writeInt(int: Int?) {
        if (int == null) {
            this.writeBoolean(false)
        } else {
            this.writeBoolean(true)
            this.buffer.writeInt(int)
        }
    }

    fun writeLong(long: Long) = this.buffer.writeLong(long)

    fun writeDouble(double: Double) = this.buffer.writeDouble(double)

    fun writeFloat(float: Float?) {
        if (float === null) {
            this.writeBoolean(false)
        } else {
            this.writeBoolean(true)
            this.buffer.writeFloat(float)
        }
    }

    fun writeString(string: String?) {
        if (string == null) {
            this.writeBoolean(false)
        } else {
            this.writeBoolean(true)
            this.buffer.writeUTF(string)
        }
    }

    fun <T : Enum<T>> writeEnum(enum: T?) = this.writeString(
        Optional.ofNullable(enum).map { it.name }.orElse(null)
    )

    fun writeUUID(uuid: UUID?) {
        if (uuid == null) {
            this.writeBoolean(false)
        } else {
            this.writeBoolean(true)
            this.writeLong(uuid.mostSignificantBits)
            this.writeLong(uuid.leastSignificantBits)
        }
    }

    fun writeByteArray(byteArray: ByteArray) {
        this.writeBoolean(true)
        this.buffer.write(byteArray)
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

        this.writeString(host)
    }

    fun writeApplication(application: Application?) {
        if (application == null) {
            this.writeBoolean(false)
        } else {
            this.writeBoolean(true)
            this.writeString(application.name)
            this.writeString(application.displayName)
            this.writeString(application.description)
            this.writeInt(application.slots)
            this.writeAddress(application.address)
            this.writeEnum(application.applicationType)
            this.writeServer(application.server)
            this.writeEnum(application.restrictJoin)
        }
    }

    fun writeServer(server: Server?) {
        if (server === null) {
            this.writeBoolean(false)
        } else {
            this.writeBoolean(true)
            this.writeString(server.getName())
        }
    }

    fun writeSerializedLocation(serializedLocation: SerializedLocation) {
        this.writeString(serializedLocation.toString())
    }

    fun writeJsonObject(jsonObject: JsonObject) {
        this.writeString(jsonObject.toString())
    }

    fun writeBaseComponent(baseComponents: Array<BaseComponent>?) {
        if (baseComponents === null) {
            this.writeString(null)
        } else {
            val serialized = ComponentSerializer.toString(*baseComponents)

            this.writeString(serialized)
        }
    }

    fun writeBaseComponent(baseComponent: BaseComponent?) {
        if (baseComponent === null) {
            this.writeBoolean(false)
        } else {
            val serialized = ComponentSerializer.toString(baseComponent)

            this.writeBoolean(true)
            this.writeString(serialized)
        }
    }

    inline fun <reified T : Serializable> writeList(list: List<T>?) {
        if (list === null) {
            this.writeBoolean(false)
        } else {
            this.writeBoolean(true)

            this.writeString(
                CoreConstants.JACKSON.writeValueAsString(
                    list
                )
            )
        }
    }

    inline fun <reified T : Serializable> writeArray(list: Array<T>?) {
        if (list === null) {
            this.writeBoolean(false)
        } else {
            this.writeBoolean(true)

            this.writeString(
                CoreConstants.JACKSON.writeValueAsString(
                    list
                )
            )
        }
    }

    fun toByteArray() = this.buffer.toByteArray()

}