package net.hyren.core.shared.misc.revoke.category.storage.repositories.implementations

import net.hyren.core.shared.misc.revoke.category.data.RevokeCategory
import net.hyren.core.shared.misc.revoke.category.storage.dao.RevokeCategoryDAO
import net.hyren.core.shared.misc.revoke.category.storage.repositories.IRevokeCategoriesRepository
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgreSQLRevokeCategoriesRepository : IRevokeCategoriesRepository {

    override fun fetchAll(): Map<String, RevokeCategory> {
        return transaction {
            val revokeCategories = mutableMapOf<String, RevokeCategory>()

            RevokeCategoryDAO.all().forEach {
                revokeCategories[it.id.value] = it.asRevokeCategory()
            }

            return@transaction revokeCategories
        }
    }

}