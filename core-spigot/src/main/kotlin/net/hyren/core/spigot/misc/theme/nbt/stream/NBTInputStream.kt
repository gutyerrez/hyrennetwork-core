package net.hyren.core.spigot.misc.theme.nbt.stream

import kotlin.experimental.and
import kotlin.reflect.KClass
import net.hyren.core.shared.misc.kotlin.sizedArray
import net.hyren.core.spigot.misc.theme.nbt.ByteArrayTag
import net.hyren.core.spigot.misc.theme.nbt.ByteTag
import net.hyren.core.spigot.misc.theme.nbt.CompoundTag
import net.hyren.core.spigot.misc.theme.nbt.DoubleTag
import net.hyren.core.spigot.misc.theme.nbt.EndTag
import net.hyren.core.spigot.misc.theme.nbt.FloatTag
import net.hyren.core.spigot.misc.theme.nbt.IntArrayTag
import net.hyren.core.spigot.misc.theme.nbt.IntTag
import net.hyren.core.spigot.misc.theme.nbt.ListTag
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
        name: String = "",
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
        9 -> {
            val childType = dataInputStream.readByte().toInt()

            val length = dataInputStream.readInt()

            val list = mutableListOf<Tag>()

            for (i in 0 until length) {
                val tag = readTagPayload(childType, depth = depth + 1)

                if (tag is EndTag) {
                    throw IOException("TAG_End not permitted in a list.")
                }

                list.add(tag)
            }

            ListTag(name, childType.getTypeClass(), list)
        }
        10 -> {
            val value = mutableMapOf<String, Tag>()

            do {
                val tag = readTag(depth + 1)

                value[tag.name] = tag
            } while (tag !is EndTag)

            CompoundTag(name, value)
        }
        11 -> {
            val length = dataInputStream.readInt()

            val value = sizedArray<Int>(length)

            for (i in 0 until length) {
                value[i] = dataInputStream.readInt()
            }

            IntArrayTag(name, value)
        }
        else -> error("Cannot decode type $type")
    }

    fun close() = dataInputStream.close()

    internal fun Int.getTypeClass(): KClass<out Tag> = when (this) {
        0 -> EndTag::class
        1 -> ByteTag::class
        2 -> ShortTag::class
        3 -> IntTag::class
        4 -> LongTag::class
        5 -> FloatTag::class
        6 -> DoubleTag::class
        7 -> ByteArrayTag::class
        8 -> StringTag::class
        9 -> ListTag::class
        10 -> CompoundTag::class
        11 -> IntArrayTag::class
        else-> error("")
    }

}