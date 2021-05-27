package net.hyren.core.shared.users.groups.due.storage.repositories

import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.groups.due.storage.dao.UserGroupDueDAO
import net.hyren.core.shared.users.groups.due.storage.dto.*

/**
 * @author SrGutyerrez
 **/
interface IUsersGroupsDueRepository : IRepository {

    fun fetchUsersGroupsDueByUserId(
        fetchUserGroupDueByUserIdDTO: FetchUserGroupDueByUserIdDTO
    ): MutableMap<Server?, MutableList<Group>>

    fun fetchUsersGroupsDueByUserIdAndServerName(
        fetchUserGroupDueByUserIdAndServerNameDTO: FetchUserGroupDueByUserIdAndServerNameDTO
    ): MutableMap<Server?, MutableList<Group>>

    fun fetchGlobalUsersGroupsDueByUserId(
        fetchGlobalUserGroupsDueByUserIdDTO: FetchGlobalUserGroupsDueByUserIdDTO
    ): MutableList<Group>

    fun create(
        createUserGroupDueDTO: CreateUserGroupDueDTO
    ): UserGroupDueDAO

    fun delete(
        deleteUserGroupDueDTO: DeleteUserGroupDueDTO
    ): Boolean

}