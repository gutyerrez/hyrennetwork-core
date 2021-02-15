package com.redefantasy.core.shared.users.passwords.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.users.passwords.data.UserPassword
import com.redefantasy.core.shared.users.passwords.storage.dto.FetchUserPasswordByUserIdDTO
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Gutyerrez
 */
class UsersPasswordsLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build<UUID, List<UserPassword>> {
            CoreProvider.Repositories.Postgres.USERS_PASSWORDS_REPOSITORY.provide().fetchByUserId(
                FetchUserPasswordByUserIdDTO(it)
            )
        }

    fun fetchById(uuid: UUID): List<UserPassword> {
        return CACHE.get(uuid) ?: emptyList()
    }

}