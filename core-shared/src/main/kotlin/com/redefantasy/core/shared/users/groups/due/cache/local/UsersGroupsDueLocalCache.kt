package com.redefantasy.core.shared.users.groups.due.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.cache.local.LocalCache
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdAndServerNameDTO
import com.redefantasy.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdDTO
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersGroupsDueLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build<UsersGroupsDueLookupCache, Map<Server?, List<Group>>> {
                if (it.server == null) {
                    CoreProvider.Repositories.Postgres.USERS_GROUPS_DUE_REPOSITORY.provide().fetchUsersGroupsDueByUserId(
                            FetchUserGroupDueByUserIdDTO(
                                    it.id
                            )
                    )
                } else {
                    CoreProvider.Repositories.Postgres.USERS_GROUPS_DUE_REPOSITORY.provide().fetchUsersGroupsDueByUserIdAndServerName(
                            FetchUserGroupDueByUserIdAndServerNameDTO(
                                    it.id,
                                    it.server
                            )
                    )
                }
            }

    fun fetchByUserId(id: UUID) = this.CACHE.get(UsersGroupsDueLookupCache(id))

    fun fetchByUserIdAndServerName(id: UUID, serverName: String) = this.CACHE.get(UsersGroupsDueLookupCache(
            id,
            CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
                    serverName
            )
    ))

    private class UsersGroupsDueLookupCache(
            val id: UUID,
            val server: Server? = null
    )

}