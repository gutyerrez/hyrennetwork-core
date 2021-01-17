package com.redefantasy.core.shared.users.friends.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.friends.data.UserFriend
import com.redefantasy.core.shared.users.friends.storage.dto.CreateUserFriendDTO
import com.redefantasy.core.shared.users.friends.storage.dto.DeleteUserFriendDTO
import com.redefantasy.core.shared.users.friends.storage.dto.FetchUsersFriendsByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersFriendsRepository : IRepository {

    fun fetchByUserId(fetchUsersFriendsByUserIdDTO: FetchUsersFriendsByUserIdDTO): List<UserFriend>

    fun create(createUserFriendDTO: CreateUserFriendDTO)

    fun delete(deleteUserFriendDTO: DeleteUserFriendDTO)

}