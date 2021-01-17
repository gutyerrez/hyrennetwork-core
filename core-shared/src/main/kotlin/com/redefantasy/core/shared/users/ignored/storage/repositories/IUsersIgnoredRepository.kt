package com.redefantasy.core.shared.users.ignored.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.ignored.data.UserIgnored
import com.redefantasy.core.shared.users.ignored.storage.dto.CreateUserFriendDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.DeleteUserFriendDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.FetchUsersIgnoredByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersIgnoredRepository : IRepository {

    fun fetchByUserId(fetchUsersIgnoredByUserIdDTO: FetchUsersIgnoredByUserIdDTO): List<UserIgnored>

    fun create(createUserFriendDTO: CreateUserFriendDTO)

    fun delete(deleteUserFriendDTO: DeleteUserFriendDTO)

}