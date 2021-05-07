package net.hyren.core.shared.misc.punish.category.storage.dao

import net.hyren.core.shared.misc.punish.category.data.PunishCategory
import net.hyren.core.shared.misc.punish.category.storage.table.PunishCategoriesTable
import net.hyren.core.shared.providers.databases.postgres.dao.StringEntity
import net.hyren.core.shared.providers.databases.postgres.dao.StringEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author SrGutyerrez
 **/
class PunishCategoryDAO(
        name: EntityID<String>
) : StringEntity(name) {

    companion object : StringEntityClass<PunishCategoryDAO>(PunishCategoriesTable)

    val displayName by PunishCategoriesTable.displayName
    val description by PunishCategoriesTable.description
    private val punishDurations by PunishCategoriesTable.punishDurations
    val group by PunishCategoriesTable.group
    val enabled by PunishCategoriesTable.enabled

    fun asPunishCategory() = PunishCategory(
            this.id,
            this.displayName,
            this.description,
            this.punishDurations,
            this.group,
            this.enabled
    )

}