package net.hyren.core.shared

import com.google.common.collect.Lists
import net.hyren.core.shared.applications.cache.local.ApplicationsLocalCache
import net.hyren.core.shared.applications.cache.redis.ApplicationsStatusRedisCache
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.storage.dto.FetchApplicationByInetSocketAddressDTO
import net.hyren.core.shared.applications.storage.repositories.IApplicationsRepository
import net.hyren.core.shared.applications.storage.repositories.implementations.PostgresApplicationsRepository
import net.hyren.core.shared.environment.Env
import net.hyren.core.shared.exceptions.ApplicationAlreadyPreparedException
import net.hyren.core.shared.exceptions.InvalidApplicationException
import net.hyren.core.shared.groups.storage.repositories.IGroupsRepository
import net.hyren.core.shared.groups.storage.repositories.implementations.PostgresGroupsRepository
import net.hyren.core.shared.misc.maintenance.cache.local.MaintenanceLocalCache
import net.hyren.core.shared.misc.maintenance.repositories.IMaintenanceRepository
import net.hyren.core.shared.misc.maintenance.repositories.implementations.PostgresMaintenanceRepository
import net.hyren.core.shared.misc.punish.category.cache.local.PunishCategoriesLocalCache
import net.hyren.core.shared.misc.punish.category.storage.repositories.IPunishCategoriesRepository
import net.hyren.core.shared.misc.punish.category.storage.repositories.implementations.PostgresPunishCategoriesRepository
import net.hyren.core.shared.misc.report.category.cache.local.ReportCategoriesLocalCache
import net.hyren.core.shared.misc.report.category.storage.repositories.IReportCategoriesRepository
import net.hyren.core.shared.misc.report.category.storage.repositories.implementations.PostgresReportCategoriesRepository
import net.hyren.core.shared.misc.revoke.category.cache.local.RevokeCategoriesLocalCache
import net.hyren.core.shared.misc.revoke.category.storage.repositories.IRevokeCategoriesRepository
import net.hyren.core.shared.misc.revoke.category.storage.repositories.implementations.PostgresRevokeCategoriesRepository
import net.hyren.core.shared.providers.IProvider
import net.hyren.core.shared.providers.cache.local.LocalCacheProvider
import net.hyren.core.shared.providers.cache.redis.RedisCacheProvider
import net.hyren.core.shared.providers.databases.influx.InfluxDatabaseProvider
import net.hyren.core.shared.providers.databases.postgres.PostgresDatabaseProvider
import net.hyren.core.shared.providers.databases.postgres.providers.PostgresRepositoryProvider
import net.hyren.core.shared.providers.databases.redis.RedisDatabaseProvider
import net.hyren.core.shared.providers.databases.redis.echo.providers.EchoProvider
import net.hyren.core.shared.servers.cache.local.ServersLocalCache
import net.hyren.core.shared.servers.storage.repositories.IServersRepository
import net.hyren.core.shared.servers.storage.repositories.implementations.PostgresServersRepository
import net.hyren.core.shared.users.cache.local.UsersLocalCache
import net.hyren.core.shared.users.cache.redis.UsersLoggedRedisCache
import net.hyren.core.shared.users.cache.redis.UsersStatusRedisCache
import net.hyren.core.shared.users.friends.cache.local.UsersFriendsLocalCache
import net.hyren.core.shared.users.friends.storage.repositories.IUsersFriendsRepository
import net.hyren.core.shared.users.friends.storage.repositories.implementations.PostgresUsersFriendsRepository
import net.hyren.core.shared.users.groups.due.cache.local.UsersGroupsDueLocalCache
import net.hyren.core.shared.users.groups.due.storage.repositories.IUsersGroupsDueRepository
import net.hyren.core.shared.users.groups.due.storage.repositories.implementations.PostgresUsersGroupsDueRepository
import net.hyren.core.shared.users.ignored.cache.local.IgnoredUsersLocalCache
import net.hyren.core.shared.users.ignored.storage.repositories.IIgnoredUsersRepository
import net.hyren.core.shared.users.ignored.storage.repositories.implementations.PostgresIgnoredUsersRepository
import net.hyren.core.shared.users.passwords.storage.repositories.IUsersPasswordsRepository
import net.hyren.core.shared.users.passwords.storage.repositories.implementations.PostgresUsersPasswordsRepository
import net.hyren.core.shared.users.preferences.cache.local.UsersPreferencesLocalCache
import net.hyren.core.shared.users.preferences.storage.repositories.IUsersPreferencesRepository
import net.hyren.core.shared.users.preferences.storage.repositories.implementations.PostgresUsersPreferencesRepository
import net.hyren.core.shared.users.punishments.cache.local.UsersPunishmentsLocalCache
import net.hyren.core.shared.users.punishments.storage.repositories.IUsersPunishmentsRepository
import net.hyren.core.shared.users.punishments.storage.repositories.implementations.PostgresUsersPunishmentsRepository
import net.hyren.core.shared.users.reports.cache.redis.UsersReportsRedisCache
import net.hyren.core.shared.users.skins.cache.local.UsersSkinsLocalCache
import net.hyren.core.shared.users.skins.storage.repositories.IUsersSkinsRepository
import net.hyren.core.shared.users.skins.storage.repositories.implementations.PostgresUsersSkinsRepository
import net.hyren.core.shared.users.storage.repositories.IUsersRepository
import net.hyren.core.shared.users.storage.repositories.implementation.PostgresUsersRepository
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
        PROVIDERS.add(Repositories.Postgres.USERS_SKINS_REPOSITORY)
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

        prepared = false
        primaryPrepared = false
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
                Env.getString("databases.postgresql.schema"),
                true
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

            val USERS_SKINS_REPOSITORY = PostgresRepositoryProvider<IUsersSkinsRepository>(
                PostgresUsersSkinsRepository::class
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