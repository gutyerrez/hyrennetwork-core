package net.hyren.core.shared

import net.hyren.core.shared.applications.cache.local.ApplicationsLocalCache
import net.hyren.core.shared.applications.cache.redis.ApplicationsStatusRedisCache
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.storage.dto.FetchApplicationByInetSocketAddressDTO
import net.hyren.core.shared.applications.storage.repositories.IApplicationsRepository
import net.hyren.core.shared.applications.storage.repositories.implementations.PostgreSQLApplicationsRepository
import net.hyren.core.shared.environment.Env
import net.hyren.core.shared.exceptions.*
import net.hyren.core.shared.groups.storage.repositories.IGroupsRepository
import net.hyren.core.shared.groups.storage.repositories.implementations.PostgreSQLGroupsRepository
import net.hyren.core.shared.misc.maintenance.cache.local.MaintenanceLocalCache
import net.hyren.core.shared.misc.maintenance.repositories.IMaintenanceRepository
import net.hyren.core.shared.misc.maintenance.repositories.implementations.PostgreSQLMaintenanceRepository
import net.hyren.core.shared.misc.punish.category.cache.local.PunishCategoriesLocalCache
import net.hyren.core.shared.misc.punish.category.storage.repositories.IPunishCategoriesRepository
import net.hyren.core.shared.misc.punish.category.storage.repositories.implementations.PostgreSQLPunishCategoriesRepository
import net.hyren.core.shared.misc.report.category.cache.local.ReportCategoriesLocalCache
import net.hyren.core.shared.misc.report.category.storage.repositories.IReportCategoriesRepository
import net.hyren.core.shared.misc.report.category.storage.repositories.implementations.PostgreSQLReportCategoriesRepository
import net.hyren.core.shared.misc.revoke.category.cache.local.RevokeCategoriesLocalCache
import net.hyren.core.shared.misc.revoke.category.storage.repositories.IRevokeCategoriesRepository
import net.hyren.core.shared.misc.revoke.category.storage.repositories.implementations.PostgreSQLRevokeCategoriesRepository
import net.hyren.core.shared.providers.IProvider
import net.hyren.core.shared.providers.cache.local.LocalCacheProvider
import net.hyren.core.shared.providers.cache.redis.RedisCacheProvider
import net.hyren.core.shared.providers.databases.postgresql.PostgreSQLDatabaseProvider
import net.hyren.core.shared.providers.databases.postgresql.providers.PostgreSQLRepositoryProvider
import net.hyren.core.shared.providers.databases.redis.RedisDatabaseProvider
import net.hyren.core.shared.providers.databases.redis.echo.providers.EchoProvider
import net.hyren.core.shared.servers.cache.local.ServersLocalCache
import net.hyren.core.shared.servers.storage.repositories.IServersRepository
import net.hyren.core.shared.servers.storage.repositories.implementations.PostgreSQLServersRepository
import net.hyren.core.shared.users.cache.local.UsersLocalCache
import net.hyren.core.shared.users.cache.redis.*
import net.hyren.core.shared.users.friends.cache.local.UsersFriendsLocalCache
import net.hyren.core.shared.users.friends.storage.repositories.IUsersFriendsRepository
import net.hyren.core.shared.users.friends.storage.repositories.implementations.PostgreSQLUsersFriendsRepository
import net.hyren.core.shared.users.groups.due.cache.local.UsersGroupsDueLocalCache
import net.hyren.core.shared.users.groups.due.storage.repositories.IUsersGroupsDueRepository
import net.hyren.core.shared.users.groups.due.storage.repositories.implementations.PostgreSQLUsersGroupsDueRepository
import net.hyren.core.shared.users.ignored.cache.local.IgnoredUsersLocalCache
import net.hyren.core.shared.users.ignored.storage.repositories.IIgnoredUsersRepository
import net.hyren.core.shared.users.ignored.storage.repositories.implementations.PostgreSQLIgnoredUsersRepository
import net.hyren.core.shared.users.passwords.storage.repositories.IUsersPasswordsRepository
import net.hyren.core.shared.users.passwords.storage.repositories.implementations.PostgreSQLUsersPasswordsRepository
import net.hyren.core.shared.users.preferences.cache.local.UsersPreferencesLocalCache
import net.hyren.core.shared.users.preferences.storage.repositories.IUsersPreferencesRepository
import net.hyren.core.shared.users.preferences.storage.repositories.implementations.PostgreSQLUsersPreferencesRepository
import net.hyren.core.shared.users.punishments.cache.local.UsersPunishmentsLocalCache
import net.hyren.core.shared.users.punishments.storage.repositories.IUsersPunishmentsRepository
import net.hyren.core.shared.users.punishments.storage.repositories.implementations.PostgreSQLUsersPunishmentsRepository
import net.hyren.core.shared.users.reports.cache.redis.UsersReportsRedisCache
import net.hyren.core.shared.users.skins.cache.local.UsersSkinsLocalCache
import net.hyren.core.shared.users.skins.storage.repositories.IUsersSkinsRepository
import net.hyren.core.shared.users.skins.storage.repositories.implementations.PostgreSQLUsersSkinsRepository
import net.hyren.core.shared.users.storage.repositories.IUsersRepository
import net.hyren.core.shared.users.storage.repositories.implementation.PostgreSQLUsersRepository
import okhttp3.Request
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
object CoreProvider {

    private val PROVIDERS = mutableListOf<IProvider<*>>()

    init {
        // repositories
        PROVIDERS.add(Repositories.PostgreSQL.GROUPS_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.USERS_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.USERS_GROUPS_DUE_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.REPORT_CATEGORIES_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.PUNISH_CATEGORIES_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.REVOKE_CATEGORIES_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.USERS_PUNISHMENTS_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.USERS_PASSWORDS_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.USERS_FRIENDS_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.IGNORED_USERS_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.USERS_PREFERENCES_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.USERS_SKINS_REPOSITORY)
        PROVIDERS.add(Repositories.PostgreSQL.MAINTENANCE_REPOSITORY)

        // local cache
        PROVIDERS.add(Cache.Local.SERVERS)
        PROVIDERS.add(Cache.Local.USERS)
        PROVIDERS.add(Cache.Local.USERS_GROUPS_DUE)
        PROVIDERS.add(Cache.Local.APPLICATIONS)
        PROVIDERS.add(Cache.Local.REPORT_CATEGORIES)
        PROVIDERS.add(Cache.Local.PUNISH_CATEGORIES)
        PROVIDERS.add(Cache.Local.REVOKE_CATEGORIES)
        PROVIDERS.add(Cache.Local.USERS_PUNISHMENTS)
        PROVIDERS.add(Cache.Local.USERS_FRIENDS)
        PROVIDERS.add(Cache.Local.USERS_IGNORED)
        PROVIDERS.add(Cache.Local.USERS_PREFERENCES)
        PROVIDERS.add(Cache.Local.USERS_SKINS)
        PROVIDERS.add(Cache.Local.MAINTENANCE)

        // redis cache
        PROVIDERS.add(Cache.Redis.APPLICATIONS_STATUS)
        PROVIDERS.add(Cache.Redis.USERS_REPORTS)
        PROVIDERS.add(Cache.Redis.USERS_STATUS)
    }

    private var prepared = false
    private var primaryPrepared = false

    lateinit var application: Application

    fun prepare(port: Int): Application {
        if (primaryPrepared)
            throw ApplicationAlreadyPreparedException("the application has already prepared")

        preparePrimaryProviders()

        val address = InetSocketAddress(
            CoreConstants.OK_HTTP.newCall(
                Request.Builder()
                    .url("https://checkip.amazonaws.com")
                    .method("GET", null)
                    .build()
            ).execute().body!!.string().trim(),
            port
        )

        val application = Repositories.PostgreSQL.APPLICATIONS_REPOSITORY.provide().fetchByInetSocketAddress(
            FetchApplicationByInetSocketAddressDTO(
                address
            )
        ) ?: throw InvalidApplicationException("Invalid application $address")

        prepare(application)

        return application
    }

    fun prepare(application: Application) {
        if (prepared)
            throw ApplicationAlreadyPreparedException("the application has already prepared")

        CoreProvider.application = application

        prepared = true

        preparePrimaryProviders()

        prepareProviders()

        Repositories.PostgreSQL.GROUPS_REPOSITORY.provide().fetchAll()
    }

    private fun preparePrimaryProviders() {
        if (!primaryPrepared) {
            primaryPrepared = true

            Databases.PostgreSQL.POSTGRESQL_MAIN.prepare()
            Databases.Redis.REDIS_MAIN.prepare()

            Repositories.PostgreSQL.SERVERS_REPOSITORY.prepare()
            Repositories.PostgreSQL.APPLICATIONS_REPOSITORY.prepare()

            Databases.Redis.ECHO.prepare()
        }
    }

    private fun prepareProviders() {
        PROVIDERS.forEach { it.prepare() }
    }

    fun shutdown() {
        PROVIDERS.forEach { it.shutdown() }

        prepared = false
        primaryPrepared = false
    }

    object Databases {

        object PostgreSQL {

            val POSTGRESQL_MAIN = PostgreSQLDatabaseProvider(
                InetSocketAddress(
                    Env.getString("databases.postgresql.host"),
                    Env.getInt("databases.postgresql.port")
                ),
                Env.getString("databases.postgresql.user"),
                Env.getString("databases.postgresql.password"),
                Env.getString("databases.postgresql.database"),
                Env.getString("databases.postgresql.schema"),
                true
            )

        }

        object Redis {

            val REDIS_MAIN = RedisDatabaseProvider(
                InetSocketAddress(
                    Env.getString("databases.redis.host"),
                    Env.getInt("databases.redis.port")
                ),
                Env.getString("databases.redis.password"),
            )

            val ECHO = EchoProvider(REDIS_MAIN)

        }

    }

    object Repositories {

        object PostgreSQL {

            val GROUPS_REPOSITORY = PostgreSQLRepositoryProvider<IGroupsRepository>(
                PostgreSQLGroupsRepository::class
            )

            val USERS_REPOSITORY = PostgreSQLRepositoryProvider<IUsersRepository>(
                PostgreSQLUsersRepository::class
            )

            val SERVERS_REPOSITORY = PostgreSQLRepositoryProvider<IServersRepository>(
                PostgreSQLServersRepository::class
            )

            val APPLICATIONS_REPOSITORY = PostgreSQLRepositoryProvider<IApplicationsRepository>(
                PostgreSQLApplicationsRepository::class
            )

            val USERS_GROUPS_DUE_REPOSITORY = PostgreSQLRepositoryProvider<IUsersGroupsDueRepository>(
                PostgreSQLUsersGroupsDueRepository::class
            )

            val REPORT_CATEGORIES_REPOSITORY = PostgreSQLRepositoryProvider<IReportCategoriesRepository>(
                PostgreSQLReportCategoriesRepository::class
            )

            val PUNISH_CATEGORIES_REPOSITORY = PostgreSQLRepositoryProvider<IPunishCategoriesRepository>(
                PostgreSQLPunishCategoriesRepository::class
            )

            val REVOKE_CATEGORIES_REPOSITORY = PostgreSQLRepositoryProvider<IRevokeCategoriesRepository>(
                PostgreSQLRevokeCategoriesRepository::class
            )

            val USERS_PUNISHMENTS_REPOSITORY = PostgreSQLRepositoryProvider<IUsersPunishmentsRepository>(
                PostgreSQLUsersPunishmentsRepository::class
            )

            val USERS_PASSWORDS_REPOSITORY = PostgreSQLRepositoryProvider<IUsersPasswordsRepository>(
                PostgreSQLUsersPasswordsRepository::class
            )

            val USERS_FRIENDS_REPOSITORY = PostgreSQLRepositoryProvider<IUsersFriendsRepository>(
                PostgreSQLUsersFriendsRepository::class
            )

            val IGNORED_USERS_REPOSITORY = PostgreSQLRepositoryProvider<IIgnoredUsersRepository>(
                PostgreSQLIgnoredUsersRepository::class
            )

            val USERS_PREFERENCES_REPOSITORY = PostgreSQLRepositoryProvider<IUsersPreferencesRepository>(
                PostgreSQLUsersPreferencesRepository::class
            )

            val USERS_SKINS_REPOSITORY = PostgreSQLRepositoryProvider<IUsersSkinsRepository>(
                PostgreSQLUsersSkinsRepository::class
            )

            val MAINTENANCE_REPOSITORY = PostgreSQLRepositoryProvider<IMaintenanceRepository>(
                PostgreSQLMaintenanceRepository::class
            )

        }

    }

    object Cache {

        object Local {

            val USERS = LocalCacheProvider(
                UsersLocalCache()
            )

            val SERVERS = LocalCacheProvider(
                ServersLocalCache()
            )

            val APPLICATIONS = LocalCacheProvider(
                ApplicationsLocalCache()
            )

            val USERS_GROUPS_DUE = LocalCacheProvider(
                UsersGroupsDueLocalCache()
            )

            val REPORT_CATEGORIES = LocalCacheProvider(
                ReportCategoriesLocalCache()
            )

            val PUNISH_CATEGORIES = LocalCacheProvider(
                PunishCategoriesLocalCache()
            )

            val REVOKE_CATEGORIES = LocalCacheProvider(
                RevokeCategoriesLocalCache()
            )

            val USERS_PUNISHMENTS = LocalCacheProvider(
                UsersPunishmentsLocalCache()
            )

            val USERS_FRIENDS = LocalCacheProvider(
                UsersFriendsLocalCache()
            )

            val USERS_IGNORED = LocalCacheProvider(
                IgnoredUsersLocalCache()
            )

            val USERS_PREFERENCES = LocalCacheProvider(
                UsersPreferencesLocalCache()
            )

            val USERS_SKINS = LocalCacheProvider(
                UsersSkinsLocalCache()
            )

            val MAINTENANCE = LocalCacheProvider(
                MaintenanceLocalCache()
            )

        }

        object Redis {

            val APPLICATIONS_STATUS = RedisCacheProvider(
                ApplicationsStatusRedisCache()
            )

            val USERS_REPORTS = RedisCacheProvider(
                UsersReportsRedisCache()
            )

            val USERS_STATUS = RedisCacheProvider(
                UsersStatusRedisCache()
            )

            val USERS_LOGGED = RedisCacheProvider(
                UsersLoggedRedisCache()
            )

        }

    }

}