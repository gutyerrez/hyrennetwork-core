package com.redefantasy.core.shared.users.data

import com.redefantasy.core.shared.CoreConstants
import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.echo.packets.DisconnectUserPacket
import com.redefantasy.core.shared.groups.Group
import com.redefantasy.core.shared.misc.punish.PunishType
import com.redefantasy.core.shared.misc.report.category.data.ReportCategory
import com.redefantasy.core.shared.misc.utils.ChatColor
import com.redefantasy.core.shared.misc.utils.DateFormatter
import com.redefantasy.core.shared.misc.utils.EncryptionUtil
import com.redefantasy.core.shared.servers.data.Server
import com.redefantasy.core.shared.users.passwords.storage.dto.FetchUserPasswordByUserIdDTO
import com.redefantasy.core.shared.users.punishments.data.UserPunishment
import com.redefantasy.core.shared.users.punishments.storage.dto.UpdateUserPunishmentByIdDTO
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import okhttp3.internal.toImmutableList
import org.jetbrains.exposed.dao.id.EntityID
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

/**
 * @author SrGutyerrez
 **/
open class User(
    open val id: EntityID<UUID>,
    open val name: String,
    open var email: String? = null,
    open var discordId: Long? = null,
    open var twoFactorAuthenticationEnabled: Boolean? = null,
    open var twoFactorAuthenticationCode: String? = null,
    open var twitterAccessToken: String? = null,
    open var twitterTokenSecret: String? = null,
    open var lastAddress: String? = null,
    open var lastLobbyName: String? = null,
    open var lastLoginAt: DateTime? = null,
    open var createdAt: DateTime? = null,
    open var updatedAt: DateTime? = null
) {

    val loginAttempts = AtomicInteger(0)
    var directMessage = CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchDirectMessage(
        this
    )

    fun setLogged(logged: Boolean) {
        CoreProvider.Cache.Redis.USERS_LOGGED.provide().setLogged(this, logged)
    }

    fun disconnect(message: Array<BaseComponent>) {
        val packet = DisconnectUserPacket()

        packet.userId = this.id.value
        packet.message = message

        CoreProvider.Databases.Redis.ECHO.provide().publishToAll(packet)
    }

    fun disconnect(message: BaseComponent) {
        val packet = DisconnectUserPacket()

        packet.userId = this.id.value
        packet.message = arrayOf(message)

        CoreProvider.Databases.Redis.ECHO.provide().publishToAll(packet)
    }

    fun validatePunishments(): Array<BaseComponent>? {
        val userPunishments = this.getPunishments()

        userPunishments.stream()
            .filter { it.revokeTime !== null && it.startTime === null }
            .forEach {
                println("Ativando a punição ${it.id.value}")

                it.startTime = DateTime.now(
                    CoreConstants.DATE_TIME_ZONE
                )

                CoreProvider.Repositories.Postgres.USERS_PUNISHMENTS_REPOSITORY.provide().update(
                    UpdateUserPunishmentByIdDTO(
                        it.id
                    ) { userPunishmentDAO ->
                        userPunishmentDAO.startTime = it.startTime
                    }
                )
            }

        val activePunishment = userPunishments.stream().filter {
            it.isActive() && it.isBan()
        }.findFirst().orElse(null)

        println(activePunishment)

        if (activePunishment !== null) {
            val staffer = CoreProvider.Cache.Local.USERS.provide().fetchById(activePunishment.stafferId)

            val message = ComponentBuilder()
                .append("§c§lREDE FANTASY")
                .append("\n\n")
                .append("§cVocê está ${activePunishment.punishType.sampleName} do servidor")
                .append("\n\n")
                .append(
                    "§cMotivo: ${activePunishment.punishCategory?.displayName ?: activePunishment.customReason}${
                        if (activePunishment.proof !== null) " - ${activePunishment.proof}"
                        else ""
                    }"
                )
                .append("\n")
                .append("§cAutor: ${staffer?.name}")

            if (activePunishment.punishType !== PunishType.BAN) {
                message.append("\n")
                    .append(
                        "§cDuração: ${
                            DateFormatter.formatToDefault(
                                activePunishment.startTime!!.withMillis(activePunishment.duration),
                                " às "
                            )
                        }"
                    )
            }

            message.append("\n\n")
                .append("§cUse o ID §b#${activePunishment.id.value} §cpara criar uma revisão em §ndiscord.gg/redefantasy§r§c.")

            return message.create()
        }

        return null
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

    fun getFancyName() = "${ChatColor.fromHEX(this.getHighestGroup().color!!)}$name"

    fun getUniqueId() = this.id.value

    fun getGroups(server: Server? = null): Map<Server?, List<Group>> {
        val _groups = mutableMapOf<Server?, List<Group>>(
            Pair(null, listOf(Group.DEFAULT))
        )

        return if (server == null) {
            CoreProvider.Cache.Local.USERS_GROUPS_DUE.provide().fetchByUserId(this.getUniqueId()) ?: _groups
        } else {
            CoreProvider.Cache.Local.USERS_GROUPS_DUE.provide()
                .fetchByUserIdAndServerName(this.getUniqueId(), server.getName()) ?: _groups
        }
    }

    fun getHighestGroup(server: Server? = null): Group {
        if (this.getUniqueId() == CoreConstants.CONSOLE_UUID) return Group.MASTER

        val groups = this.getGroups()[server]

        if (groups === null) return Group.DEFAULT

        return groups
            .stream()
            .sorted { group1, group2 ->
                group2.priority!!.compareTo(group1.priority!!)
            }
            .findFirst()
            .orElse(Group.DEFAULT)
    }

    fun hasGroup(group: Group, server: Server? = null): Boolean {
        if (this.getUniqueId() == CoreConstants.CONSOLE_UUID) return true

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

        if (groups === null || groups.isEmpty()) return false

        return groups.stream().anyMatch { it.priority!! >= group.priority!! }
    }

    fun hasStrictGroup(group: Group, server: Server? = null): Boolean {
        if (this.getUniqueId() == CoreConstants.CONSOLE_UUID) return true

        return this.getGroups(server)[server]?.contains(group) ?: false
    }

    fun getConnectedProxyName() =
        CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchProxyApplication(this)?.displayName ?: "Desconhecido"

    fun getConnectedAddress() =
        CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchConnectedAddress(this) ?: "Desconhecido"

    fun getConnectedBukkitApplication(): Application? {
        return CoreProvider.Cache.Redis.USERS_STATUS.provide().fetchBukkitApplication(this)
    }

    fun isOnline() = CoreProvider.Cache.Redis.USERS_STATUS.provide().isOnline(this)

    fun getPunishments() = CoreProvider.Cache.Local.USERS_PUNISHMENTS.provide().fetchByUserId(this.id) ?: emptyList()

    fun getActivePunishments(): List<UserPunishment> {
        this.validatePunishments()

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
        val _friends = CoreProvider.Cache.Local.USERS_FRIENDS.provide().fetchByUserId(this.id) ?: emptyList()

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

    fun getFriendRequests(): List<User> {
        return (CoreProvider.Cache.Local.USERS_FRIENDS.provide().fetchFriendRequestsByUserId(this.id) ?: emptyList())
            .stream()
            .filter {
                this.getFriends().stream().anyMatch { user -> user.id != it.userId }
            }
            .filter {
                CoreProvider.Cache.Local.USERS.provide().fetchById(it.friendUserId) !== null
            }
            .map {
                CoreProvider.Cache.Local.USERS.provide().fetchById(it.friendUserId)!!
            }
            .collect(Collectors.toList())
            .toImmutableList()
    }

    fun getIgnoredUsers(): List<User> {
        val ignored = mutableListOf<User>()
        val _ignored = CoreProvider.Cache.Local.USERS_IGNORED.provide().fetchByUserId(this.id) ?: emptyList()

        _ignored.stream()
            .map {
                CoreProvider.Cache.Local.USERS.provide().fetchById(
                    it.ignoredUserId
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
            this.id
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

    fun isMuted() = getActivePunishments()
        .stream()
        .filter { it.punishType === PunishType.MUTE }
        .findFirst()
        .orElse(null)

    fun isAFriendOf(user: User) = user.getFriends().contains(this) && this.getFriends().contains(user)

    override fun equals(other: Any?): Boolean {
        if (other === null) return false

        if (this === other) return true

        if (javaClass != other.javaClass) return false

        other as User

        println(id.value)
        println(other.id.value)

        if (id.value != other.id.value) return false

        return true
    }

    override fun hashCode() = this.id.value.hashCode()

}