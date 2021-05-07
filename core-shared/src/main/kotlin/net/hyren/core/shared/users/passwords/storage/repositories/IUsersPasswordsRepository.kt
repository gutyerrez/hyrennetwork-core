package net.hyren.core.shared.users.passwords.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.passwords.data.UserPassword
import net.hyren.core.shared.users.passwords.storage.dto.CreateUserPasswordDTO
import net.hyren.core.shared.users.passwords.storage.dto.FetchUserPasswordByUserIdDTO

/**
 * @author Gutyerrez
 */
interface IUsersPasswordsRepository : IRepository {

    fun fetchByUserId(fetchUserPasswordByUserIdDTO: FetchUserPasswordByUserIdDTO): List<UserPassword>

    fun create(createUserPasswordDTO: CreateUserPasswordDTO)

}