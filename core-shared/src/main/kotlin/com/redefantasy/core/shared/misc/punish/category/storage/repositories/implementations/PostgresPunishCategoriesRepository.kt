package com.redefantasy.core.shared.misc.punish.category.storage.repositories.implementations

import com.redefantasy.core.shared.misc.punish.category.data.PunishCategory
import com.redefantasy.core.shared.misc.punish.category.storage.dao.PunishCategoryDAO
import com.redefantasy.core.shared.misc.punish.category.storage.repositories.IPunishCategoriesRepository
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresPunishCategoriesRepository : IPunishCategoriesRepository {

    override fun fetchAll(): Map<String, PunishCategory> {
        return transaction {
            val categories = mutableMapOf<String, PunishCategory>()

            PunishCategoryDAO.all().forEach {
                categories[it.id.value] = it.asPunishCategory()
            }

            return@transaction categories
        }
    }

}