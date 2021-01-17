package com.redefantasy.core.shared.users.skins.storage.repositories

import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByUserIdDTO

/**
 * @author SrGutyerrez
 **/
interface IUsersSkinsRepository {

    fun fetchByUserId(fetchUserSkinByUserIdDTO: FetchUserSkinByUserIdDTO): List<UserSkin>

    fun fetchByName(fetchUserSkinByNameDTO: FetchUserSkinByNameDTO): UserSkin?

    fun create(createUserSkinDTO: CreateUserSkinDTO)

}