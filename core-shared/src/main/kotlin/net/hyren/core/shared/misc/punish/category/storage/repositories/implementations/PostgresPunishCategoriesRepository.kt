package net.hyren.core.shared.misc.punish.category.storage.repositories.implementations

import net.hyren.core.shared.misc.punish.category.data.PunishCategory
import net.hyren.core.shared.misc.punish.category.storage.dao.PunishCategoryDAO
import net.hyren.core.shared.misc.punish.category.storage.repositories.IPunishCategoriesRepository
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