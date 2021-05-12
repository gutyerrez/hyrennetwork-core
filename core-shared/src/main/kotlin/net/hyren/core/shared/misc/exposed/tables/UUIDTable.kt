package net.hyren.core.shared.misc.exposed.tables

import net.hyren.core.shared.misc.exposed.customUUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

/**
 * @author Gutyerrez
 */
open class UUIDTable(name: String = "", columnName: String = "id") : IdTable<UUID>(name) {

    override val id: Column<EntityID<UUID>> = customUUID(columnName)

    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }

}
