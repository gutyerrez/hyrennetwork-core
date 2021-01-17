package com.redefantasy.core.shared.groups.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository

/**
 * @author SrGutyerrez
 **/
interface IGroupsRepository : IRepository {

    fun fetchAll()

}