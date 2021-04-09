package com.redefantasy.core.shared.users.skins.storage.repositories

import com.redefantasy.core.shared.storage.repositories.IRepository
import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinsByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersSkinsRepository : IRepository {

    fun fetchByUserId(
        fetchUserSkinsByUserIdDTO: FetchUserSkinsByUserIdDTO
    ): List<UserSkin>

    fun fetchByName(
        fetchUserSkinByNameDTO: FetchUserSkinByNameDTO
    ): UserSkin?

    fun create(
        createUserSkinDTO: CreateUserSkinDTO
    )

}