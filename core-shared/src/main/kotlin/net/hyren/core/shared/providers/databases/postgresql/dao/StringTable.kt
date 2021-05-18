package net.hyren.core.shared.providers.databases.postgresql.dao

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

/**
 * @author SrGutyerrez
 **/
open class StringTable(
    name: String, columnName: String = "name"
) : IdTable<String>(name) {

    override val id: Column<EntityID<String>> = varchar(columnName, 255).entityId()

    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }

}