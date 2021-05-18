package net.hyren.core.shared.users.groups.due.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdAndServerNameDTO
import net.hyren.core.shared.users.groups.due.storage.dto.FetchUserGroupDueByUserIdDTO
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
                    CoreProvider.Repositories.PostgreSQL.USERS_GROUPS_DUE_REPOSITORY.provide().fetchUsersGroupsDueByUserId(
                            FetchUserGroupDueByUserIdDTO(
                                    it.id
                            )
                    )
                } else {
                    CoreProvider.Repositories.PostgreSQL.USERS_GROUPS_DUE_REPOSITORY.provide().fetchUsersGroupsDueByUserIdAndServerName(
                            FetchUserGroupDueByUserIdAndServerNameDTO(
                                    it.id,
                                    it.server
                            )
                    )
                }
            }

    fun fetchByUserId(id: UUID) = this.CACHE.get(UsersGroupsDueLookupCache(id))

    fun fetchByUserIdAndServerName(id: UUID, serverName: String) = this.CACHE.get(
        UsersGroupsDueLookupCache(
            id,
            CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
                    serverName
            )
    )
    )

    private class UsersGroupsDueLookupCache(
            val id: UUID,
            val server: Server? = null
    )

}