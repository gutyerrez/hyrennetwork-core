package net.hyren.core.shared.misc.punish.category.storage.repositories.implementations

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.misc.punish.category.data.PunishCategory
import net.hyren.core.shared.misc.punish.category.storage.dao.PunishCategoryDAO
import net.hyren.core.shared.misc.punish.category.storage.repositories.IPunishCategoriesRepository
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgreSQLPunishCategoriesRepository : IPunishCategoriesRepository {

    override fun fetchAll() = transaction(
        CoreProvider.Databases.PostgreSQL.POSTGRESQL_MAIN.provide()
    ) {
        val categories = mutableMapOf<String, PunishCategory>()

        PunishCategoryDAO.all().forEach {
            categories[it.id.value] = it.toPunishCategory()
        }

        categories
    }

}