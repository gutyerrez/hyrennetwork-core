package com.redefantasy.core.shared.users.friends.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.friends.data.FriendUser
import com.redefantasy.core.shared.users.friends.storage.dto.CreateFriendUserDTO
import com.redefantasy.core.shared.users.friends.storage.dto.DeleteFriendUserDTO
import com.redefantasy.core.shared.users.friends.storage.dto.FetchFriendRequestsByUserIdDTO
import com.redefantasy.core.shared.users.friends.storage.dto.FetchFriendUsersByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersFriendsRepository : IRepository {

    fun fetchByUserId(
        fetchFriendUsersByUserIdDTO: FetchFriendUsersByUserIdDTO
    ): List<FriendUser>

    fun fetchFriendRequestsByUserId(
        fetchFriendRequestsByUserId: FetchFriendRequestsByUserIdDTO
    ): List<FriendUser>

    fun create(createFriendUserDTO: CreateFriendUserDTO)

    fun delete(deleteFriendUserDTO: DeleteFriendUserDTO)

}