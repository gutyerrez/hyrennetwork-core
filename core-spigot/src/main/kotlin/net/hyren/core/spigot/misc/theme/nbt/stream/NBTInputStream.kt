package net.hyren.core.spigot.misc.theme.nbt.stream

import kotlin.experimental.and
import net.hyren.core.spigot.misc.theme.nbt.ByteArrayTag
import net.hyren.core.spigot.misc.theme.nbt.ByteTag
import net.hyren.core.spigot.misc.theme.nbt.CompoundTag
import net.hyren.core.spigot.misc.theme.nbt.DoubleTag
import net.hyren.core.spigot.misc.theme.nbt.EndTag
import net.hyren.core.spigot.misc.theme.nbt.FloatTag
import net.hyren.core.spigot.misc.theme.nbt.IntTag
import net.hyren.core.spigot.misc.theme.nbt.LongTag
import net.hyren.core.spigot.misc.theme.nbt.ShortTag
import net.hyren.core.spigot.misc.theme.nbt.StringTag
import net.hyren.core.spigot.misc.theme.nbt.Tag
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream

class NBTInputStream(inputStream: InputStream): Cloneable {

    val dataInputStream: DataInputStream

    init {
        dataInputStream = DataInputStream(inputStream)
    }

    fun readTag() = readTag(0)

    fun readTag(depth: Int): Tag {
        val type = (dataInputStream.readByte() and 0xFF.toByte()).toInt()

        lateinit var name: String

        if (type != 0x00) {
            val nameLength = (dataInputStream.readShort() and 0xFFFF.toShort()).toInt()

            val byteArray = ByteArray(nameLength)

            dataInputStream.readFully(byteArray)

            name = String(byteArray)
        } else {
            name = ""
        }

        return readTagPayload(type, name, depth)
    }

    fun readTagPayload(
        type: Int,
        name: String,
        depth: Int
    ): Tag = when (type) {
        0 -> {
            if (depth == 0) {
                throw IOException("TAG_End found without a TAG_Compound/TAG_List tag preceding it.")
            }

            EndTag()
        }
        1 -> ByteTag(name, dataInputStream.readByte())
        2 -> ShortTag(name, dataInputStream.readShort())
        3 -> IntTag(name, dataInputStream.readInt())
        4 -> LongTag(name, dataInputStream.readLong())
        5 -> FloatTag(name, dataInputStream.readFloat())
        6 -> DoubleTag(name, dataInputStream.readDouble())
        7 -> {
            val size = dataInputStream.readInt()

            val byteArray = ByteArray(size)

            dataInputStream.readFully(byteArray)

            ByteArrayTag(name, byteArray)
        }
        8 -> {
            val length = dataInputStream.readShort().toInt()

            val byteArray = ByteArray(length)

            dataInputStream.readFully(byteArray)

            StringTag(name, String(
                byteArray
            ))
        }
        10 -> {
            val value = mutableMapOf<String, Tag>()

            do {
                val tag = readTag(depth + 1)

                value[tag.name] = tag
            } while (tag !is EndTag)

            CompoundTag(name, value)
        }
        else -> error("Cannot decode type $type")
    }

    fun close() = dataInputStream.close()

}