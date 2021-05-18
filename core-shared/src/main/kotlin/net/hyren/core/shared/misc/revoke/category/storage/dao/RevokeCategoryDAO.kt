package net.hyren.core.shared.misc.revoke.category.storage.dao

import net.hyren.core.shared.misc.revoke.category.data.RevokeCategory
import net.hyren.core.shared.misc.revoke.category.storage.table.RevokeCategoriesTable
import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntity
import net.hyren.core.shared.providers.databases.postgresql.dao.StringEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class RevokeCategoryDAO(
        name: EntityID<String>
) : StringEntity(name) {

    companion object : StringEntityClass<RevokeCategoryDAO>(RevokeCategoriesTable)

    val displayName by RevokeCategoriesTable.displayName
    val description by RevokeCategoriesTable.description
    val group by RevokeCategoriesTable.group
    val enabled by RevokeCategoriesTable.enabled

    fun asRevokeCategory() = RevokeCategory(
            this.id,
            this.displayName,
            this.description,
            this.group,
            this.enabled
    )

}