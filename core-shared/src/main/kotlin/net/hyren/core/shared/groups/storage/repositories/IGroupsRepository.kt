package net.hyren.core.shared.groups.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IGroupsRepository : IRepository {

    fun fetchAll()

}