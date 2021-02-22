package com.redefantasy.core.shared.users.data

import com.google.common.primitives.Ints
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.report.category.data.ReportCategory
import com.redefantasy.core.shared.misc.utils.EncryptionUtil
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.users.passwords.storage.dto.FetchUserPasswordByUserIdDTO
import com.redefantasy.core.shared.users.punishments.data.UserPunishment
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

/**
 * @author SrGutyerrez
 **/
data class User(
    val id: EntityID<UUID>,
    val name: String,
    var email: String? = null,
    var discordId: Long? = null,
    var twoFactorAuthenticationEnabled: Boolean? = null,
    var twoFactorAuthenticationCode: String? = null,
    var twitterAccessToken: String? = null,
    var twitterTokenSecret: String? = null,
    var lastAddress: String? = null,
    var lastLobbyName: String? = null,
    var lastLoginAt: DateTime? = null,
    var createdAt: DateTime? = null,
    var updatedAt: DateTime? = null
) {

    val loginAttempts = AtomicInteger(0)

    fun setLogged(logged: Boolean) {
        CoreProvider.Cache.Redis.USERS_LOGGED.provide().setLogged(this, logged)
    }

    fun attemptLogin(password: String): Boolean {
        val userPasswords = CoreProvider.Repositories.Postgres.USERS_PASSWORDS_REPOSITORY.provide().fetchByUserId(
            FetchUserPasswordByUserIdDTO(this.getUniqueId())
        )

        if (userPasswords.isEmpty()) return false

        val successfully = userPasswords.stream().anyMatch {
            it.enabled && it.password.contentEquals(
                EncryptionUtil.hash(
                    EncryptionUtil.Type.SHA256,
                    password
                )
            )
        }

        if (!successfully) loginAttempts.getAndIncrement()

        return successfully
    }

    fun getUniqueId() = this.id.value

    fun getGroups(server: Server? = null): Map<Server?, List<Group>> {
        val _groups = mutableMapOf<Server?, List<Group>>(
            Pair(null, listOf(Group.DEFAULT))
        )

        println("Grupos: $_groups")

        return if (server == null) {
            CoreProvider.Cache.Local.USERS_GROUPS_DUE.provide().fetchByUserId(this.getUniqueId()) ?: _groups
        } else {
            CoreProvider.Cache.Local.USERS_GROUPS_DUE.provide()
                .fetchByUserIdAndServerName(this.getUniqueId(), server.getName()) ?: _groups
        }
    }

    fun getHighestGroup(server: Server? = null): Group {
        val groups = this.getGroups()[server]

        if (groups === null) return Group.DEFAULT

        return groups
            .stream()
            .sorted { o1, o2 -> Ints.compare(o2.priority!!, o1.priority!!) }
            .findFirst()
            .orElse(Group.DEFAULT)
    }

    fun hasGroup(group: Group, server: Server? = null): Boolean {
        val groups = if (server === null) {
            val groups = mutableListOf<Group>()

            this.getGroups().entries.forEach {
                groups.addAll(
                    it.value
                        .stream()
                        .distinct()
                        .collect(Collectors.toList())
                )
            }

            groups
        } else this.getGroups()[server]

        println(groups)

        println(groups?.isEmpty())

        if (groups === null || groups.isEmpty()) return false

        return groups.stream().anyMatch { it.priority!! >= group.priority!! }
    }

    fun hasStrictGroup(group: Group, server: Server? = null): Boolean {
        return this.getGroups(server).contains(group)
    }

    fun getConnectedProxyName(): String {
        return CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchProxyApplication(this)?.displayName
            ?: "Desconhecido"
    }

    fun getConnectedAddress(): String {
        return CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchConnectedAddress(this) ?: "Desconhecido"
    }

    fun getConnectedBukkitApplication(): Application? {
        return CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchBukkitApplication(this)
    }

    fun isOnline(): Boolean {
        return CoreProvider.Cache.Redis.USERS_STATUS.provide().isOnline(this)
    }

    fun getPunishments(): List<UserPunishment> {
        return CoreProvider.Cache.Local.USERS_PUNISHMENTS.provide().fetchByUserId(this.getUniqueId()) ?: emptyList()
    }

    fun getActivePunishments(): List<UserPunishment> {
        return this.getPunishments()
            .stream()
            .filter { it.isActive() }
            .collect(Collectors.toList())
    }

    fun isBanned(): Boolean {
        return this.getActivePunishments()
            .stream()
            .anyMatch { it.isBan() }
    }

    fun getFriends(): List<User> {
        val friends = mutableListOf<User>()
        val _friends = CoreProvider.Cache.Local.USERS_FRIENDS.provide().fetchByUserId(this.getUniqueId()) ?: emptyList()

        _friends.stream()
            .map {
                CoreProvider.Cache.Local.USERS.provide().fetchById(
                    it.friendUserId
                )
            }
            .forEach {
                if (it != null) friends.add(it)
            }

        return friends
    }

    fun getIgnored(): List<User> {
        val ignored = mutableListOf<User>()
        val _ignored = CoreProvider.Cache.Local.USERS_IGNORED.provide().fetchByUserId(this.getUniqueId()) ?: emptyList()

        _ignored.stream()
            .map {
                CoreProvider.Cache.Local.USERS.provide().fetchById(
                    it.friendUserId
                )
            }
            .forEach {
                if (it != null) ignored.add(it)
            }

        return ignored
    }

    fun getPreferences(): Map<String, Boolean> {
        val preferences = mutableMapOf<String, Boolean>()

        val userPreferences = CoreProvider.Cache.Local.USERS_PREFERENCES.provide().fetchByUserId(
            this.getUniqueId()
        ) ?: emptyList()

        userPreferences.forEach { preferences[it.preference.name] = it.status }

        return preferences
    }

    fun getReports(): Map<ReportCategory, Int> {
        val reports = mutableMapOf<ReportCategory, Int>()

        CoreProvider.Cache.Redis.USERS_REPORTS.provide().fetchByUserId(
            this.getUniqueId()
        ).forEach {
            val count = reports[it.reportCategory] ?: 0

            reports[it.reportCategory] = count + 1
        }

        return reports
    }

    fun isLogged() = CoreProvider.Cache.Redis.USERS_LOGGED.provide().isLogged(this)

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode() = this.id.hashCode()

}