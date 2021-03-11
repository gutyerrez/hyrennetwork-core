package com.redefantasy.core.spigot.misc.spawn.repositories.table

import com.redefantasy.core.shared.applications.storage.table.ApplicationsTable
import com.redefantasy.core.shared.world.location.SerializedLocation
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

/**
 * @author Gutyerrez
 */
object SpawnTable : Table("applications_spawn") {

    val applicationName = reference("application_name", ApplicationsTable)
    val worldName = varchar("world", 20)
    val x = double("x")
    val y = double("y")
    val z = double("z")
    val yaw = float("yaw")
    val pitch = float("pitch")

    fun ResultRow.asSerializedLocation() = SerializedLocation(
        this[applicationName].value,
        this[worldName],
        this[x],
        this[y],
        this[z],
        this[yaw],
        this[pitch]
    )

}