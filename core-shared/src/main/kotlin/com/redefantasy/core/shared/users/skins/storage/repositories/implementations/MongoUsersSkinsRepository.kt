package com.redefantasy.core.shared.users.skins.storage.repositories.implementations

import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByUserIdDTO
import com.redefantasy.core.shared.users.skins.storage.repositories.IUsersSkinsRepository

/**
 * @author SrGutyerrez
 **/
class MongoUsersSkinsRepository : IUsersSkinsRepository {

    override fun fetchByUserId(
        fetchUserSkinByUserIdDTO: FetchUserSkinByUserIdDTO
    ): List<UserSkin> {
        TODO("Not implemented-yet")
    }

    override fun fetchByName(
        fetchUserSkinByNameDTO: FetchUserSkinByNameDTO
    ): UserSkin? {
        TODO("Not yet implemented")
    }

    override fun create(
        createUserSkinDTO: CreateUserSkinDTO
    ) {
        TODO("Not implemented-yet")
    }

}