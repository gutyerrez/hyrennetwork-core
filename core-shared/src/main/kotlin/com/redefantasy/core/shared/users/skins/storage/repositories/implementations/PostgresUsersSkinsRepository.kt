package com.redefantasy.core.shared.users.skins.storage.repositories.implementations

import com.redefantasy.core.shared.users.skins.data.UserSkin
import com.redefantasy.core.shared.users.skins.storage.dao.UserSkinDAO
import com.redefantasy.core.shared.users.skins.storage.dto.CreateUserSkinDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByNameDTO
import com.redefantasy.core.shared.users.skins.storage.dto.FetchUserSkinByUserIdDTO
import com.redefantasy.core.shared.users.skins.storage.repositories.IUsersSkinsRepository
import com.redefantasy.core.shared.users.skins.storage.table.UsersSkinsTable
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author SrGutyerrez
 **/
class PostgresUsersSkinsRepository : IUsersSkinsRepository {

    override fun fetchByUserId(fetchUserSkinByUserIdDTO: FetchUserSkinByUserIdDTO): List<UserSkin> {
        TODO("Not yet implemented")
    }

    override fun fetchByName(fetchUserSkinByNameDTO: FetchUserSkinByNameDTO): UserSkin? {
        return transaction {
            val result = UserSkinDAO.find {
                UsersSkinsTable.name eq fetchUserSkinByNameDTO.name
            }

            return@transaction if (result.empty()) null else result.first().asPlayerSkin()
        }
    }

    override fun create(createUserSkinDTO: CreateUserSkinDTO) {
        transaction {
            UserSkinDAO.new {
                this.name = createUserSkinDTO.userSkin.name
                this.signature = createUserSkinDTO.userSkin.skin.signature
                this.value = createUserSkinDTO.userSkin.skin.value
            }
        }
    }

}