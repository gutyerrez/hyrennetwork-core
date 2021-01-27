package com.redefantasy.core.shared.users.storage.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime

/**
 * @author SrGutyerrez
 **/
object UsersTable : UUIDTable("users") {

    val name = varchar("name", 16)
    val password = varchar("password", 255).nullable()
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