package net.hyren.core.shared.users.friends.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.friends.data.FriendUser
import net.hyren.core.shared.users.friends.storage.dto.CreateFriendUserDTO
import net.hyren.core.shared.users.friends.storage.dto.DeleteFriendUserDTO
import net.hyren.core.shared.users.friends.storage.dto.FetchFriendRequestsByUserIdDTO
import net.hyren.core.shared.users.friends.storage.dto.FetchFriendUsersByUserIdDTO

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