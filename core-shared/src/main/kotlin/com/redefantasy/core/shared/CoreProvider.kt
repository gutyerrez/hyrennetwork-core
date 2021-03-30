package com.redefantasy.core.shared

import com.google.common.collect.Lists
import com.redefantasy.core.shared.applications.cache.local.ApplicationsLocalCache
import com.redefantasy.core.shared.applications.cache.redis.ApplicationsStatusRedisCache
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.applications.storage.dto.FetchApplicationByInetSocketAddressDTO
import com.redefantasy.core.shared.applications.storage.repositories.IApplicationsRepository
import com.redefantasy.core.shared.applications.storage.repositories.implementations.PostgresApplicationsRepository
import com.redefantasy.core.shared.environment.Env
import com.redefantasy.core.shared.exceptions.ApplicationAlreadyPreparedException
import com.redefantasy.core.shared.exceptions.InvalidApplicationException
import com.redefantasy.core.shared.groups.storage.repositories.IGroupsRepository
import com.redefantasy.core.shared.groups.storage.repositories.implementations.PostgresGroupsRepository
import com.redefantasy.core.shared.misc.maintenance.cache.local.MaintenanceLocalCache
import com.redefantasy.core.shared.misc.maintenance.repositories.IMaintenanceRepository
import com.redefantasy.core.shared.misc.maintenance.repositories.implementations.PostgresMaintenanceRepository
import com.redefantasy.core.shared.misc.punish.category.cache.local.PunishCategoriesLocalCache
import com.redefantasy.core.shared.misc.punish.category.storage.repositories.IPunishCategoriesRepository
import com.redefantasy.core.shared.misc.punish.category.storage.repositories.implementations.PostgresPunishCategoriesRepository
import com.redefantasy.core.shared.misc.report.category.cache.local.ReportCategoriesLocalCache
import com.redefantasy.core.shared.misc.report.category.storage.repositories.IReportCategoriesRepository
import com.redefantasy.core.shared.misc.report.category.storage.repositories.implementations.PostgresReportCategoriesRepository
import com.redefantasy.core.shared.misc.revoke.category.cache.local.RevokeCategoriesLocalCache
import com.redefantasy.core.shared.misc.revoke.category.storage.repositories.IRevokeCategoriesRepository
import com.redefantasy.core.shared.misc.revoke.category.storage.repositories.implementations.PostgresRevokeCategoriesRepository
import com.redefantasy.core.shared.providers.IProvider
import com.redefantasy.core.shared.providers.cache.local.LocalCacheProvider
import com.redefantasy.core.shared.providers.cache.redis.RedisCacheProvider
import com.redefantasy.core.shared.providers.databases.influx.InfluxDatabaseProvider
import com.redefantasy.core.shared.providers.databases.postgres.PostgresDatabaseProvider
import com.redefantasy.core.shared.providers.databases.postgres.providers.PostgresRepositoryProvider
import com.redefantasy.core.shared.providers.databases.redis.RedisDatabaseProvider
import com.redefantasy.core.shared.providers.databases.redis.echo.providers.EchoProvider
import com.redefantasy.core.shared.servers.cache.local.ServersLocalCache
import com.redefantasy.core.shared.servers.storage.repositories.IServersRepository
import com.redefantasy.core.shared.servers.storage.repositories.implementations.PostgresServersRepository
import com.redefantasy.core.shared.users.cache.local.UsersLocalCache
import com.redefantasy.core.shared.users.cache.redis.UsersLoggedRedisCache
import com.redefantasy.core.shared.users.cache.redis.UsersStatusRedisCache
import com.redefantasy.core.shared.users.friends.cache.local.UsersFriendsLocalCache
import com.redefantasy.core.shared.users.friends.storage.repositories.IUsersFriendsRepository
import com.redefantasy.core.shared.users.friends.storage.repositories.implementations.PostgresUsersFriendsRepository
import com.redefantasy.core.shared.users.groups.due.cache.local.UsersGroupsDueLocalCache
import com.redefantasy.core.shared.users.groups.due.storage.repositories.IUsersGroupsDueRepository
import com.redefantasy.core.shared.users.groups.due.storage.repositories.implementations.PostgresUsersGroupsDueRepository
import com.redefantasy.core.shared.users.ignored.cache.local.IgnoredUsersLocalCache
import com.redefantasy.core.shared.users.ignored.storage.repositories.IIgnoredUsersRepository
import com.redefantasy.core.shared.users.ignored.storage.repositories.implementations.PostgresIgnoredUsersRepository
import com.redefantasy.core.shared.users.passwords.storage.repositories.IUsersPasswordsRepository
import com.redefantasy.core.shared.users.passwords.storage.repositories.implementations.PostgresUsersPasswordsRepository
import com.redefantasy.core.shared.users.preferences.cache.local.UsersPreferencesLocalCache
import com.redefantasy.core.shared.users.preferences.storage.repositories.IUsersPreferencesRepository
import com.redefantasy.core.shared.users.preferences.storage.repositories.implementations.PostgresUsersPreferencesRepository
import com.redefantasy.core.shared.users.punishments.cache.local.UsersPunishmentsLocalCache
import com.redefantasy.core.shared.users.punishments.storage.repositories.IUsersPunishmentsRepository
import com.redefantasy.core.shared.users.punishments.storage.repositories.implementations.PostgresUsersPunishmentsRepository
import com.redefantasy.core.shared.users.reports.cache.redis.UsersReportsRedisCache
import com.redefantasy.core.shared.users.storage.repositories.IUsersRepository
import com.redefantasy.core.shared.users.storage.repositories.implementation.PostgresUsersRepository
import org.apache.commons.io.IOUtils
import java.net.InetSocketAddress
import java.net.URL

/**
 * @author SrGutyerrez
 **/
object CoreProvider {

    private val PROVIDERS = Lists.newArrayList<IProvider<*>>()

    init {
        // repositories
        PROVIDERS.add(Repositories.Postgres.GROUPS_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.USERS_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.USERS_GROUPS_DUE_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.REPORT_CATEGORIES_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.PUNISH_CATEGORIES_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.REVOKE_CATEGORIES_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.USERS_PUNISHMENTS_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.USERS_PASSWORDS_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.USERS_FRIENDS_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.IGNORED_USERS_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.USERS_PREFERENCES_REPOSITORY)
        PROVIDERS.add(Repositories.Postgres.MAINTENANCE_REPOSITORY)

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

        this.preparePrimaryProviders()

        val address = InetSocketAddress(
            IOUtils.toString(
                URL("http://checkip.amazonaws.com"),
                Charsets.UTF_8
            ).trim(),
            port
        )

        val application = Repositories.Postgres.APPLICATIONS_REPOSITORY.provide().fetchByInetSocketAddress(
            FetchApplicationByInetSocketAddressDTO(
                address
            )
        ) ?: throw InvalidApplicationException("Invalid application $address")

        this.prepare(application)

        return application
    }

    fun prepare(application: Application) {
        if (prepared)
            throw ApplicationAlreadyPreparedException("the application has already prepared")

        this.application = application

        prepared = true

        this.preparePrimaryProviders()

        this.prepareProviders()

        Repositories.Postgres.GROUPS_REPOSITORY.provide().fetchAll()
    }

    private fun preparePrimaryProviders() {
        if (!primaryPrepared) {
            primaryPrepared = true

            Databases.Postgres.POSTGRE_MAIN.prepare()
            Databases.Redis.REDIS_MAIN.prepare()
            Databases.Redis.REDIS_ECHO.prepare()
            Databases.Influx.INFLUX_MAIN.prepare()

            Repositories.Postgres.SERVERS_REPOSITORY.prepare()
            Repositories.Postgres.APPLICATIONS_REPOSITORY.prepare()

            Databases.Redis.ECHO.prepare()
        }
    }

    private fun prepareProviders() {
        PROVIDERS.forEach { it.prepare() }
    }

    fun shutdown() {
        PROVIDERS.forEach { it.shutdown() }
    }

    object Databases {

        object Postgres {

            val POSTGRE_MAIN = PostgresDatabaseProvider(
                InetSocketAddress(
                    Env.getString("databases.postgresql.host"),
                    Env.getInt("databases.postgresql.port")
                ),
                Env.getString("databases.postgresql.user"),
                Env.getString("databases.postgresql.password"),
                Env.getString("databases.postgresql.database"),
                Env.getString("databases.postgresql.schema")
            )

        }

        object Redis {

            val REDIS_MAIN = RedisDatabaseProvider(
                InetSocketAddress(
                    Env.getString("databases.redis.main.host"),
                    Env.getInt("databases.redis.main.port")
                ),
                Env.getString("databases.redis.main.password"),
            )

            val REDIS_ECHO = RedisDatabaseProvider(
                InetSocketAddress(
                    Env.getString("databases.redis.echo.host"),
                    Env.getInt("databases.redis.echo.port")
                ),
                Env.getString("databases.redis.echo.password")
            )

            val ECHO = EchoProvider(REDIS_ECHO)

        }

        object Influx {

            val INFLUX_MAIN = InfluxDatabaseProvider(
                InetSocketAddress(
                    Env.getString("databases.influx_db.host"),
                    Env.getInt("databases.influx_db.port")
                ),
                Env.getString("databases.influx_db.user"),
                Env.getString("databases.influx_db.password"),
                Env.getString("databases.influx_db.database")
            )

        }

    }

    object Repositories {

        object Postgres {

            val GROUPS_REPOSITORY = PostgresRepositoryProvider<IGroupsRepository>(
                PostgresGroupsRepository::class
            )

            val USERS_REPOSITORY = PostgresRepositoryProvider<IUsersRepository>(
                PostgresUsersRepository::class
            )

            val SERVERS_REPOSITORY = PostgresRepositoryProvider<IServersRepository>(
                PostgresServersRepository::class
            )

            val APPLICATIONS_REPOSITORY = PostgresRepositoryProvider<IApplicationsRepository>(
                PostgresApplicationsRepository::class
            )

            val USERS_GROUPS_DUE_REPOSITORY = PostgresRepositoryProvider<IUsersGroupsDueRepository>(
                PostgresUsersGroupsDueRepository::class
            )

            val REPORT_CATEGORIES_REPOSITORY = PostgresRepositoryProvider<IReportCategoriesRepository>(
                PostgresReportCategoriesRepository::class
            )

            val PUNISH_CATEGORIES_REPOSITORY = PostgresRepositoryProvider<IPunishCategoriesRepository>(
                PostgresPunishCategoriesRepository::class
            )

            val REVOKE_CATEGORIES_REPOSITORY = PostgresRepositoryProvider<IRevokeCategoriesRepository>(
                PostgresRevokeCategoriesRepository::class
            )

            val USERS_PUNISHMENTS_REPOSITORY = PostgresRepositoryProvider<IUsersPunishmentsRepository>(
                PostgresUsersPunishmentsRepository::class
            )

            val USERS_PASSWORDS_REPOSITORY = PostgresRepositoryProvider<IUsersPasswordsRepository>(
                PostgresUsersPasswordsRepository::class
            )

            val USERS_FRIENDS_REPOSITORY = PostgresRepositoryProvider<IUsersFriendsRepository>(
                PostgresUsersFriendsRepository::class
            )

            val IGNORED_USERS_REPOSITORY = PostgresRepositoryProvider<IIgnoredUsersRepository>(
                PostgresIgnoredUsersRepository::class
            )

            val USERS_PREFERENCES_REPOSITORY = PostgresRepositoryProvider<IUsersPreferencesRepository>(
                PostgresUsersPreferencesRepository::class
            )

            val MAINTENANCE_REPOSITORY = PostgresRepositoryProvider<IMaintenanceRepository>(
                PostgresMaintenanceRepository::class
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