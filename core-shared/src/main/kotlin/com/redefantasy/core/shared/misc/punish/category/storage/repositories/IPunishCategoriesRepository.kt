package com.redefantasy.core.shared.misc.punish.category.storage.repositories

import com.redefantasy.core.shared.misc.punish.category.data.PunishCategory
import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IPunishCategoriesRepository : IRepository {

    fun fetchAll(): Map<String, PunishCategory>

}