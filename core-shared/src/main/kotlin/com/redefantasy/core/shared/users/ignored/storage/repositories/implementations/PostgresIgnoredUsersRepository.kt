package com.redefantasy.core.shared.users.ignored.storage.repositories.implementations

import com.redefantasy.core.shared.users.ignored.data.IgnoredUser
import com.redefantasy.core.shared.users.ignored.storage.dto.CreateIgnoredUserDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.DeleteIgnoredUserDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.FetchIgnoredUsersByUserIdDTO
import com.redefantasy.core.shared.users.ignored.storage.repositories.IIgnoredUsersRepository

/**
 * @author SrGutyerrez
 **/
class PostgresIgnoredUsersRepository : IIgnoredUsersRepository {

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