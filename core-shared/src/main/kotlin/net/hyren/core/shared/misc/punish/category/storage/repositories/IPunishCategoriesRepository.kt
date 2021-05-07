package net.hyren.core.shared.misc.punish.category.storage.repositories

import net.hyren.core.shared.misc.punish.category.data.PunishCategory
import net.hyren.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IPunishCategoriesRepository : IRepository {

    fun fetchAll(): Map<String, PunishCategory>

}