package com.redefantasy.core.shared.users.passwords.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.passwords.data.UserPassword
import com.redefantasy.core.shared.users.passwords.storage.dto.CreateUserPasswordDTO
import com.redefantasy.core.shared.users.passwords.storage.dto.FetchUserPasswordByUserIdDTO

/**
 * @author Gutyerrez
 */
interface IUsersPasswordsRepository : IRepository {

    fun fetchByUserId(fetchUserPasswordByUserIdDTO: FetchUserPasswordByUserIdDTO): List<UserPassword>

    fun create(createUserPasswordDTO: CreateUserPasswordDTO)

}