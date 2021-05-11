package net.hyren.core.shared.users.friends.storage.repositories.implementations

import net.hyren.core.shared.users.friends.data.FriendUser
import net.hyren.core.shared.users.friends.storage.dto.CreateFriendUserDTO
import net.hyren.core.shared.users.friends.storage.dto.DeleteFriendUserDTO
import net.hyren.core.shared.users.friends.storage.dto.FetchFriendRequestsByUserIdDTO
import net.hyren.core.shared.users.friends.storage.dto.FetchFriendUsersByUserIdDTO
import net.hyren.core.shared.users.friends.storage.repositories.IUsersFriendsRepository

/**
 * @author SrGutyerrez
 **/
class MariaDBUsersFriendsRepository : IUsersFriendsRepository {

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