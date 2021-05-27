package net.hyren.core.shared.users.groups.due.cache.local

import com.github.benmanes.caffeine.cache.Caffeine
import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.cache.local.LocalCache
import net.hyren.core.shared.groups.Group
import net.hyren.core.shared.servers.data.Server
import net.hyren.core.shared.users.groups.due.storage.dto.*
import net.hyren.core.shared.users.storage.table.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author SrGutyerrez
 **/
class UsersGroupsDueLocalCache : LocalCache {

    private val CACHE = Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build<UsersGroupsDueLookupCache, Map<Server?, List<Group>>> {
            val groups = if (it.server == null) {
                CoreProvider.Repositories.PostgreSQL.USERS_GROUPS_DUE_REPOSITORY.provide().fetchUsersGroupsDueByUserId(
                    FetchUserGroupDueByUserIdDTO(
                        it.userId
                    )
                )
            } else {
                CoreProvider.Repositories.PostgreSQL.USERS_GROUPS_DUE_REPOSITORY.provide().fetchUsersGroupsDueByUserIdAndServerName(
                    FetchUserGroupDueByUserIdAndServerNameDTO(
                        it.userId, it.server
                    )
                )
            }

            if (!it.isStrict) {
                groups.put(
                    it.server,
                    CoreProvider.Repositories.PostgreSQL.USERS_GROUPS_DUE_REPOSITORY.provide().fetchGlobalUsersGroupsDueByUserId(
                        FetchGlobalUserGroupsDueByUserIdDTO(
                            it.userId
                        )
                    )
                )
            }

            groups
        }

    fun fetchByUserId(
        id: EntityID<UUID>
    ) = this.CACHE.get(
        UsersGroupsDueLookupCache(
            id
        )
    )

    fun fetchByUserId(
        id: UUID
    ) = this.CACHE.get(
        UsersGroupsDueLookupCache(
            EntityID(
                id,
                UsersTable
            )
        )
    )

    fun fetchByUserIdAndServerName(
        id: EntityID<UUID>,
        serverName: String
    ) = this.CACHE.get(
        UsersGroupsDueLookupCache(
            id,
            CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
                serverName
            )
        )
    )

    fun fetchByUserIdAndServerName(
        id: UUID,
        serverName: String
    ) = this.CACHE.get(
        UsersGroupsDueLookupCache(
            EntityID(
                id, UsersTable
            ),
            CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
                serverName
            )
        )
    )

    fun fetchStrictByUserIdAndServerName(
        id: EntityID<UUID>,
        serverName: String
    ) = this.CACHE.get(
        UsersGroupsDueLookupCache(
            id,
            CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
                serverName
            ),
            true
        )
    )

    fun fetchStrictByUserIdAndServerName(
        id: UUID,
        serverName: String
    ) = this.CACHE.get(
        UsersGroupsDueLookupCache(
            EntityID(
                id, UsersTable
            ),
            CoreProvider.Cache.Local.SERVERS.provide().fetchByName(
                serverName
            ),
            true
        )
    )

    private class UsersGroupsDueLookupCache(
        val userId: EntityID<UUID>,
        val server: Server? = null,
        val isStrict: Boolean = false
    )

}