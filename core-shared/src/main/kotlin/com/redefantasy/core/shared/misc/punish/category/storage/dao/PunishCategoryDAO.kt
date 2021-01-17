package com.redefantasy.core.shared.misc.punish.category.storage.dao

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.misc.punish.category.data.PunishCategory
import com.redefantasy.core.shared.misc.punish.category.storage.table.PunishCategoriesTable
import com.redefantasy.core.shared.misc.punish.durations.PunishDuration
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntity
import com.redefantasy.core.shared.providers.databases.postgres.dao.StringEntityClass
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
            this.id.value,
            this.displayName,
            this.description,
            com.redefantasy.core.shared.CoreConstants.JACKSON.readValue(
                    this.punishDurations,
                    Array<PunishDuration>::class.java
            ),
            this.group,
            this.enabled
    )

}