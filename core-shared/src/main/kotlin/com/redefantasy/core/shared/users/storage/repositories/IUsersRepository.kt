package com.redefantasy.core.shared.users.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.data.User
import com.redefantasy.core.shared.users.storage.dto.*

/**
 * @author SrGutyerrez
 **/
interface IUsersRepository : IRepository {
    
    fun fetchById(fetchUserById: FetchUserByIdDTO): User?

    fun fetchByName(fetchUserByName: FetchUserByNameDTO): User?

    fun fetchByDiscordId(fetchUserByDiscordId: FetchUserByDiscordIdDTO): User?

    fun create(createUserDTO: CreateUserDTO)

    fun <E> update(updateUserByIdDTO: UpdateUserByIdDTO<E>)

}