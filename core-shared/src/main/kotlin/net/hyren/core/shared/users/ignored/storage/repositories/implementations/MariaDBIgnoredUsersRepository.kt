package net.hyren.core.shared.users.ignored.storage.repositories.implementations

import net.hyren.core.shared.users.ignored.data.IgnoredUser
import net.hyren.core.shared.users.ignored.storage.dto.CreateIgnoredUserDTO
import net.hyren.core.shared.users.ignored.storage.dto.DeleteIgnoredUserDTO
import net.hyren.core.shared.users.ignored.storage.dto.FetchIgnoredUsersByUserIdDTO
import net.hyren.core.shared.users.ignored.storage.repositories.IIgnoredUsersRepository

/**
 * @author SrGutyerrez
 **/
class MariaDBIgnoredUsersRepository : IIgnoredUsersRepository {

    override fun fetchByUserId(
        fetchIgnoredUsersByUserIdDTO: FetchIgnoredUsersByUserIdDTO
    ): List<IgnoredUser> {
        TODO("not implemented")
    }

    override fun create(
        createIgnoredUserDTO: CreateIgnoredUserDTO
    ) {
        TODO("not implemented")
    }

    override fun delete(
        deleteIgnoredUserDTO: DeleteIgnoredUserDTO
    ) {
        TODO("not implemented")
    }

}