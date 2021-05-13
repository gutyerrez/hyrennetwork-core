package net.hyren.core.shared.misc.exposed.table

import net.hyren.core.shared.misc.exposed.uuid4
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import java.util.*

/**
 * @author Gutyerrez
 */
open class UUIDTable(name: String = "", columnName: String = "id") : IdTable<UUID>(name) {

    override val id: Column<EntityID<UUID>> = uuid4(columnName)
        .autoGenerate()
        .entityId()

    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }

}