package com.redefantasy.core.shared.users.friends.storage.repositories.implementations

import com.redefantasy.core.shared.users.friends.data.FriendUser
import com.redefantasy.core.shared.users.friends.storage.dto.CreateFriendUserDTO
import com.redefantasy.core.shared.users.friends.storage.dto.DeleteFriendUserDTO
import com.redefantasy.core.shared.users.friends.storage.dto.FetchFriendRequestsByUserIdDTO
import com.redefantasy.core.shared.users.friends.storage.dto.FetchFriendUsersByUserIdDTO
import com.redefantasy.core.shared.users.friends.storage.repositories.IUsersFriendsRepository

/**
 * @author SrGutyerrez
 **/
class PostgresUsersFriendsRepository : IUsersFriendsRepository {

    override fun fetchByUserId(
        fetchFriendUsersByUserIdDTO: FetchFriendUsersByUserIdDTO
    ): List<FriendUser> {
        TODO("not implemented")
    }

    override fun fetchFriendRequestsByUserId(
        fetchFriendRequestsByUserId: FetchFriendRequestsByUserIdDTO
    ): List<FriendUser> {
        TODO("not implemented")
    }

    override fun create(
        createFriendUserDTO: CreateFriendUserDTO
    ) {
        TODO("not implemented")
    }

    override fun delete(
        deleteFriendUserDTO: DeleteFriendUserDTO
    ) {
        TODO("not implemented")
    }

}