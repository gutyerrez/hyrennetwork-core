package net.hyren.core.shared.users.skins.storage.repositories

import net.hyren.core.shared.storage.repositories.IRepository
import net.hyren.core.shared.users.skins.data.UserSkin
import net.hyren.core.shared.users.skins.storage.dto.*

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

    fun fetchByUserIdAndName(
        fetchUserSkinByUserIdAndNameDTO: FetchUserSkinByUserIdAndNameDTO
    ): UserSkin?

    fun create(
        createUserSkinDTO: CreateUserSkinDTO
    ): UserSkin

    fun update(
        updateUserSkinDTO: UpdateUserSkinDTO
    )

}