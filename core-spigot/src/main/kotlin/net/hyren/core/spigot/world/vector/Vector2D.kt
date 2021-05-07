package net.hyren.core.spigot.world.vector

import org.bukkit.util.Vector
import kotlin.math.*

class Vector2D {

    val x: Double
    val z: Double

    companion object {

        val ZERO = Vector2D(0, 0)
        val UNIT_X = Vector2D(1, 0)
        val UNIT_Z = Vector2D(0, 1)
        val ONE = Vector2D(1, 1)

        fun getMinimum(v1: Vector2D, v2: Vector2D): Vector2D {
            return Vector2D(
                Math.min(v1.x, v2.x),
                Math.min(v1.z, v2.z)
            )
        }

        fun getMaximum(v1: Vector2D, v2: Vector2D): Vector2D {
            return Vector2D(
                Math.max(v1.x, v2.x),
                Math.max(v1.z, v2.z)
            )
        }
    }

    constructor(x: Double, z: Double) {
        this.x = x
        this.z = z
    }

    constructor(x: Int, z: Int) {
        this.x = x.toDouble()
        this.z = z.toDouble()
    }

    constructor(x: Float, z: Float) {
        this.x = x.toDouble()
        this.z = z.toDouble()
    }

    constructor(other: Vector2D) {
        x = other.x
        z = other.z
    }

    constructor() {
        x = 0.0
        z = 0.0
    }

    val blockX: Int get() = x.roundToInt()

    fun setX(x: Double) = Vector2D(x, z)

    fun setX(x: Int) = Vector2D(x.toDouble(), z)

    val blockZ: Int get() = z.roundToInt()

    fun setZ(z: Double) = Vector2D(x, z)

    fun setZ(z: Int) = Vector2D(x, z.toDouble())

    fun add(other: Vector2D) = Vector2D(x + other.x, z + other.z)

    fun add(x: Double, z: Double) = Vector2D(this.x + x, this.z + z)

    fun add(x: Int, z: Int) = Vector2D(this.x + x, this.z + z)

    fun add(vararg others: Vector2D): Vector2D {
        var newX = x
        var newZ = z

        for (other in others) {
            newX += other.x
            newZ += other.z
        }

        return Vector2D(newX, newZ)
    }

    fun subtract(other: Vector2D) = Vector2D(x - other.x, z - other.z)

    fun subtract(x: Double, z: Double) = Vector2D(this.x - x, this.z - z)

    fun subtract(x: Int, z: Int) = Vector2D(this.x - x, this.z - z)

    fun subtract(vararg others: Vector2D): Vector2D {
        var newX = x
        var newZ = z

        for (other in others) {
            newX -= other.x
            newZ -= other.z
        }

        return Vector2D(newX, newZ)
    }

    fun multiply(other: Vector2D) = Vector2D(x * other.x, z * other.z)

    fun multiply(x: Double, z: Double) = Vector2D(this.x * x, this.z * z)

    fun multiply(x: Int, z: Int) = Vector2D(this.x * x, this.z * z)

    fun multiply(vararg others: Vector2D): Vector2D {
        var newX = x
        var newZ = z

        for (other in others) {
            newX *= other.x
            newZ *= other.z
        }

        return Vector2D(newX, newZ)
    }

    fun multiply(n: Double) =  Vector2D(x * n, z * n)

    fun multiply(n: Float) = Vector2D(x * n, z * n)

    fun multiply(n: Int) = Vector2D(x * n, z * n)

    fun divide(other: Vector2D) = Vector2D(x / other.x, z / other.z)

    fun divide(x: Double, z: Double) = Vector2D(this.x / x, this.z / z)

    fun divide(x: Int, z: Int) = Vector2D(this.x / x, this.z / z)

    fun divide(n: Int) = Vector2D(x / n, z / n)

    fun divide(n: Double) = Vector2D(x / n, z / n)

    fun divide(n: Float) = Vector2D(x / n, z / n)

    fun length() = sqrt(x * x + z * z)

    fun lengthSq() = x * x + z * z

    fun distance(other: Vector2D) = sqrt((other.x - x).pow(2.0) + (other.z - z).pow(2.0))

    fun distanceSq(other: Vector2D) = (other.x - x).pow(2.0) + (other.z - z).pow(2.0)

    fun normalize() = this.divide(this.length())

    fun dot(other: Vector2D) = x * other.x + z * other.z

    fun containedWithin(
        min: Vector2D,
        max: Vector2D
    ) = x >= min.x && x <= max.x && z >= min.z && z <= max.z

    fun containedWithinBlock(
        min: Vector2D,
        max: Vector2D
    ) = blockX >= min.blockX && blockX <= max.blockX && blockZ >= min.blockZ && blockZ <= max.blockZ

    fun floor() = Vector2D(floor(x), floor(z))

    fun ceil() = Vector2D(ceil(x), ceil(z))

    fun round() = Vector2D(floor(x + 0.5), floor(z + 0.5))

    fun positive() = Vector2D(abs(x), abs(z))

    fun transform2D(angle: Double, aboutX: Double, aboutZ: Double, translateX: Double, translateZ: Double): Vector2D {
        var angle = angle

        angle = Math.toRadians(angle)
        val x = x - aboutX
        val z = z - aboutZ
        val x2 = x * cos(angle) - z * sin(angle)
        val z2 = x * sin(angle) + z * cos(angle)

        return Vector2D(
            x2 + aboutX + translateX,
            z2 + aboutZ + translateZ
        )
    }

    fun isCollinearWith(other: Vector2D): Boolean {
        if (x == 0.0 && z == 0.0) return true

        val otherX = other.x
        val otherZ = other.z

        if (otherX == 0.0 && otherZ == 0.0) return true

        if (x == 0.0 != (otherX == 0.0) || z == 0.0 != (otherZ == 0.0)) return false

        val quotientX = otherX / x

        if (!quotientX.isNaN()) return other == this.multiply(quotientX)

        val quotientZ = otherZ / z

        if (!quotientZ.isNaN()) return other == multiply(quotientZ)

        throw RuntimeException("This should not happen")
    }

    fun toVector() = Vector(x, 0.0, z)

    fun toVector(y: Double) = Vector(x, y, z)

    override fun equals(obj: Any?): Boolean {
        if (obj !is Vector2D) return false

        return obj.x == x && obj.z == z
    }

    override fun hashCode(): Int {
        return x.toInt() shl 16 xor z.toInt()
    }

}