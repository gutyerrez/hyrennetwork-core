package net.hyren.core.spigot.misc.utils

import kotlin.math.abs
import kotlin.math.roundToInt
import org.bukkit.block.BlockFace
import org.bukkit.util.Vector

/**
 * @author Gutyerrez
 */
object DirectionUtils {

    private val AXIAL = arrayOf(
        BlockFace.NORTH,
        BlockFace.EAST,
        BlockFace.SOUTH,
        BlockFace.WEST
    )

    private val RADIAL = arrayOf(
        BlockFace.NORTH,
        BlockFace.NORTH_EAST,
        BlockFace.EAST,
        BlockFace.SOUTH_EAST,
        BlockFace.SOUTH,
        BlockFace.SOUTH_WEST,
        BlockFace.WEST,
        BlockFace.NORTH_WEST
    )

    fun yawToFace(yaw: Float, useSubCardinalDirections: Boolean = true) = if (useSubCardinalDirections) {
        when (RADIAL[(yaw / 45f).roundToInt() and 0x7]) {
            BlockFace.NORTH -> BlockFace.SOUTH
            BlockFace.SOUTH -> BlockFace.NORTH
            BlockFace.EAST -> BlockFace.WEST
            BlockFace.WEST -> BlockFace.EAST
            BlockFace.NORTH_WEST -> BlockFace.SOUTH_WEST
            BlockFace.NORTH_EAST -> BlockFace.SOUTH_EAST
            BlockFace.SOUTH_WEST -> BlockFace.NORTH_EAST
            BlockFace.SOUTH_EAST -> BlockFace.NORTH_WEST
            else -> throw NullPointerException()
        }
    } else {
        AXIAL[(yaw / 90f).roundToInt() and 0x3]
    }

    fun vectorToFace(vector: Vector): BlockFace = if (abs(vector.x) > abs(vector.z)) {
        if (vector.x > 0) {
            BlockFace.EAST
        } else {
            BlockFace.WEST
        }
    } else if (vector.z > 0) {
        BlockFace.SOUTH
    } else {
        BlockFace.NORTH
    }

}