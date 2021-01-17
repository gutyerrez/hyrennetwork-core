package com.redefantasy.core.shared.misc.revoke.category.storage.repositories.implementations

import com.redefantasy.core.shared.misc.revoke.category.data.RevokeCategory
import com.redefantasy.core.shared.misc.revoke.category.storage.dao.RevokeCategoryDAO
import com.redefantasy.core.shared.misc.revoke.category.storage.repositories.IRevokeCategoriesRepository
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresRevokeCategoriesRepository : IRevokeCategoriesRepository {

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