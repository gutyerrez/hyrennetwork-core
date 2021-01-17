package com.redefantasy.core.shared.users.groups.due.storage.repositories

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdAndServerNameDTO
import com.redefantasy.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersGroupsDueRepository : IRepository {

    fun fetchUsersGroupsDueByUserId(
            fetchUserGroupDueByUserIdDTO: FetchUserGroupDueByUserIdDTO
    ): List<Group>

    fun fetchUsersGroupsDueByUserIdAndServerName(
            fetchUserGroupDueByUserIdAndServerNameDTO: FetchUserGroupDueByUserIdAndServerNameDTO
    ): List<Group>

}