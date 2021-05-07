package net.hyren.core.shared.users.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.data.User
import net.hyren.core.shared.users.storage.dto.*

/**
 * @author SrGutyerrez
 **/
interface IUsersRepository : IRepository {
    
    fun fetchById(fetchUserById: FetchUserByIdDTO): User?

    fun fetchByName(fetchUserByName: FetchUserByNameDTO): User?

    fun fetchByDiscordId(fetchUserByDiscordId: FetchUserByDiscordIdDTO): User?

    fun fetchByLastAddress(fetchUserByLastAddress: FetchUserByLastAddressDTO): List<User>

    fun create(createUserDTO: CreateUserDTO): User

    fun update(updateUserByIdDTO: UpdateUserByIdDTO)

}