package com.redefantasy.core.shared.users.ignored.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.ignored.data.IgnoredUser
import com.redefantasy.core.shared.users.ignored.storage.dto.CreateIgnoredUserDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.DeleteIgnoredUserDTO
import com.redefantasy.core.shared.users.ignored.storage.dto.FetchIgnoredUsersByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersIgnoredRepository : IRepository {

    fun fetchByUserId(
        fetchIgnoredUsersByUserIdDTO: FetchIgnoredUsersByUserIdDTO
    ): List<IgnoredUser>

    fun create(createIgnoredUserDTO: CreateIgnoredUserDTO)

    fun delete(deleteIgnoredUserDTO: DeleteIgnoredUserDTO)

}