package net.hyren.core.shared.users.groups.due.storage.repositories

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.groups.due.storage.dao.UserGroupDueDAO
import net.hyren.core.shared.users.groups.due.storage.dto.CreateUserGroupDueDTO
import net.hyren.core.shared.users.groups.due.storage.dto.DeleteUserGroupDueDTO
import net.hyren.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdAndServerNameDTO
import net.hyren.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdDTO

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

    fun create(
            createUserGroupDueDTO: CreateUserGroupDueDTO
    ): UserGroupDueDAO

    fun delete(
            deleteUserGroupDueDTO: DeleteUserGroupDueDTO
    ): Boolean

}