package com.redefantasy.core.shared.users.groups.due.storage.repositories

import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdAndServerNameDTO
import com.redefantasy.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersGroupsDueRepository : IRepository {

    fun fetchUsersGroupsDueByUserId(
            fetchUserGroupDueByUserIdDTO: FetchUserGroupDueByUserIdDTO
    ): Map<Server?, List<Group>>

    fun fetchUsersGroupsDueByUserIdAndServerName(
            fetchUserGroupDueByUserIdAndServerNameDTO: FetchUserGroupDueByUserIdAndServerNameDTO
    ): Map<Server?, List<Group>>

}