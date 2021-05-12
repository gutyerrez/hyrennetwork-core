package net.hyren.core.shared.users.storage.table

import net.hyren.core.shared.misc.exposed.customUUID
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import java.util.*

/**
 * @author SrGutyerrez
 **/
object UsersTable : UUIDTable("users") {

    override val id: Column<EntityID<UUID>> = customUUID("id").entityId()

    override val primaryKey by lazy { PrimaryKey(id) }

    val name = varchar("name", 16)
    var email = varchar("email", 255).nullable()
    val discordId = long("discord_id").nullable()
    val twoFactorAuthenticationEnabled = bool("two_factor_authentication_enabled").nullable()
    val twoFactorAuthenticationCode = varchar("two_factor_authentication_code", 8).nullable()
    val twitterAccessToken = varchar("twitter_access_token", 255).nullable()
    val twitterTokenSecret = varchar("twitter_token_secret", 255).nullable()
    val lastAddress = varchar("last_address", 255).nullable()
    val lastLobbyName = varchar("last_lobby_name", 255).nullable()
    val lastLoginAt = datetime("last_login_at").nullable()
    val createdAt = datetime("created_at").nullable()
    val updatedAt = datetime("updated_at").nullable()

}