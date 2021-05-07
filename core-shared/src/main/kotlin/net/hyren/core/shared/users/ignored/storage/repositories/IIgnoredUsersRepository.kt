package net.hyren.core.shared.users.ignored.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.ignored.data.IgnoredUser
import net.hyren.core.shared.users.ignored.storage.dto.CreateIgnoredUserDTO
import net.hyren.core.shared.users.ignored.storage.dto.DeleteIgnoredUserDTO
import net.hyren.core.shared.users.ignored.storage.dto.FetchIgnoredUsersByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IIgnoredUsersRepository : IRepository {

    fun fetchByUserId(
        fetchIgnoredUsersByUserIdDTO: FetchIgnoredUsersByUserIdDTO
    ): List<IgnoredUser>

    fun create(createIgnoredUserDTO: CreateIgnoredUserDTO)

    fun delete(deleteIgnoredUserDTO: DeleteIgnoredUserDTO)

}