package net.hyren.core.shared.misc.revoke.category.storage.repositories

import net.hyren.core.shared.misc.revoke.category.data.RevokeCategory
import net.hyren.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IRevokeCategoriesRepository : IRepository {

    fun fetchAll(): Map<String, RevokeCategory>

}