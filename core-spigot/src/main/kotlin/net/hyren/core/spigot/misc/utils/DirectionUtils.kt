package net.hyren.core.spigot.misc.utils

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
    fun yawToFace(yaw: Float) = when (yawToFace(yaw, true)) {
        BlockFace.NORTH -> BlockFace.SOUTH
        BlockFace.SOUTH -> BlockFace.NORTH
        BlockFace.EAST -> BlockFace.WEST
        BlockFace.WEST -> BlockFace.EAST
        BlockFace.NORTH_WEST -> BlockFace.SOUTH_WEST
        BlockFace.NORTH_EAST -> BlockFace.SOUTH_EAST
        BlockFace.SOUTH_WEST -> BlockFace.NORTH_EAST
        BlockFace.SOUTH_EAST -> BlockFace.NORTH_WEST
        else -> null
    }

    fun yawToFace(yaw: Float, useSubCardinalDirections: Boolean) = if (useSubCardinalDirections) {
        RADIAL[Math.round(yaw / 45f) and 0x7]
    } else {
        AXIAL[Math.round(yaw / 90f) and 0x3]
    }

    fun vectorToFace(vector: Vector): BlockFace {
        val absX = Math.abs(vector.x)
        val absZ = Math.abs(vector.z)

        val x = vector.x
        val z = vector.z

        return if (absX > absZ) {
            if (x > 0) {
                BlockFace.EAST
            } else {
                BlockFace.WEST
            }
        } else if (z > 0) {
            BlockFace.SOUTH
        } else {
            BlockFace.NORTH
        }
    }

}