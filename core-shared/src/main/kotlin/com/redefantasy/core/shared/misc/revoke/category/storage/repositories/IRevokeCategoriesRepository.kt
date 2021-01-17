package com.redefantasy.core.shared.misc.revoke.category.storage.repositories

import com.redefantasy.core.shared.misc.revoke.category.data.RevokeCategory
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IRevokeCategoriesRepository : IRepository {

    fun fetchAll(): Map<String, RevokeCategory>

}