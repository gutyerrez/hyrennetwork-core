package net.hyren.core.shared

import com.google.common.collect.Lists
import net.hyren.core.shared.applications.cache.local.ApplicationsLocalCache
import net.hyren.core.shared.applications.cache.redis.ApplicationsStatusRedisCache
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.applications.storage.dto.FetchApplicationByInetSocketAddressDTO
import net.hyren.core.shared.applications.storage.repositories.IApplicationsRepository
import net.hyren.core.shared.applications.storage.repositories.implementations.MariaDBApplicationsRepository
import net.hyren.core.shared.environment.Env
import net.hyren.core.shared.exceptions.ApplicationAlreadyPreparedException
import net.hyren.core.shared.exceptions.InvalidApplicationException
import net.hyren.core.shared.groups.storage.repositories.IGroupsRepository
import net.hyren.core.shared.groups.storage.repositories.implementations.MariaDBGroupsRepository
import net.hyren.core.shared.misc.maintenance.cache.local.MaintenanceLocalCache
import net.hyren.core.shared.misc.maintenance.repositories.IMaintenanceRepository
import net.hyren.core.shared.misc.maintenance.repositories.implementations.MariaDBMaintenanceRepository
import net.hyren.core.shared.misc.punish.category.cache.local.PunishCategoriesLocalCache
import net.hyren.core.shared.misc.punish.category.storage.repositories.IPunishCategoriesRepository
import net.hyren.core.shared.misc.punish.category.storage.repositories.implementations.MariaDBPunishCategoriesRepository
import net.hyren.core.shared.misc.report.category.cache.local.ReportCategoriesLocalCache
import net.hyren.core.shared.misc.report.category.storage.repositories.IReportCategoriesRepository
import net.hyren.core.shared.misc.report.category.storage.repositories.implementations.MariaDBReportCategoriesRepository
import net.hyren.core.shared.misc.revoke.category.cache.local.RevokeCategoriesLocalCache
import net.hyren.core.shared.misc.revoke.category.storage.repositories.IRevokeCategoriesRepository
import net.hyren.core.shared.misc.revoke.category.storage.repositories.implementations.MariaDBRevokeCategoriesRepository
import net.hyren.core.shared.providers.IProvider
import net.hyren.core.shared.providers.cache.local.LocalCacheProvider
import net.hyren.core.shared.providers.cache.redis.RedisCacheProvider
import net.hyren.core.shared.providers.databases.influx.InfluxDatabaseProvider
import net.hyren.core.shared.providers.databases.mariadb.MariaDBDatabaseProvider
import net.hyren.core.shared.providers.databases.mariadb.providers.MariaDBRepositoryProvider
import net.hyren.core.shared.providers.databases.redis.RedisDatabaseProvider
import net.hyren.core.shared.providers.databases.redis.echo.providers.EchoProvider
import net.hyren.core.shared.servers.cache.local.ServersLocalCache
import net.hyren.core.shared.servers.storage.repositories.IServersRepository
import net.hyren.core.shared.servers.storage.repositories.implementations.MariaDBServersRepository
import net.hyren.core.shared.users.cache.local.UsersLocalCache
import net.hyren.core.shared.users.cache.redis.UsersLoggedRedisCache
import net.hyren.core.shared.users.cache.redis.UsersStatusRedisCache
import net.hyren.core.shared.users.friends.cache.local.UsersFriendsLocalCache
import net.hyren.core.shared.users.friends.storage.repositories.IUsersFriendsRepository
import net.hyren.core.shared.users.friends.storage.repositories.implementations.MariaDBUsersFriendsRepository
import net.hyren.core.shared.users.groups.due.cache.local.UsersGroupsDueLocalCache
import net.hyren.core.shared.users.groups.due.storage.repositories.IUsersGroupsDueRepository
import net.hyren.core.shared.users.groups.due.storage.repositories.implementations.MariaDBUsersGroupsDueRepository
import net.hyren.core.shared.users.ignored.cache.local.IgnoredUsersLocalCache
import net.hyren.core.shared.users.ignored.storage.repositories.IIgnoredUsersRepository
import net.hyren.core.shared.users.ignored.storage.repositories.implementations.MariaDBIgnoredUsersRepository
import net.hyren.core.shared.users.passwords.storage.repositories.IUsersPasswordsRepository
import net.hyren.core.shared.users.passwords.storage.repositories.implementations.MariaDBUsersPasswordsRepository
import net.hyren.core.shared.users.preferences.cache.local.UsersPreferencesLocalCache
import net.hyren.core.shared.users.preferences.storage.repositories.IUsersPreferencesRepository
import net.hyren.core.shared.users.preferences.storage.repositories.implementations.MariaDBUsersPreferencesRepository
import net.hyren.core.shared.users.punishments.cache.local.UsersPunishmentsLocalCache
import net.hyren.core.shared.users.punishments.storage.repositories.IUsersPunishmentsRepository
import net.hyren.core.shared.users.punishments.storage.repositories.implementations.MariaDBUsersPunishmentsRepository
import net.hyren.core.shared.users.reports.cache.redis.UsersReportsRedisCache
import net.hyren.core.shared.users.skins.cache.local.UsersSkinsLocalCache
import net.hyren.core.shared.users.skins.storage.repositories.IUsersSkinsRepository
import net.hyren.core.shared.users.skins.storage.repositories.implementations.MariaDBUsersSkinsRepository
import net.hyren.core.shared.users.storage.repositories.IUsersRepository
import net.hyren.core.shared.users.storage.repositories.implementation.MariaDBUsersRepository
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
        PROVIDERS.add(Repositories.MariaDB.GROUPS_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.USERS_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.USERS_GROUPS_DUE_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.REPORT_CATEGORIES_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.PUNISH_CATEGORIES_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.REVOKE_CATEGORIES_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.USERS_PUNISHMENTS_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.USERS_PASSWORDS_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.USERS_FRIENDS_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.IGNORED_USERS_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.USERS_PREFERENCES_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.USERS_SKINS_REPOSITORY)
        PROVIDERS.add(Repositories.MariaDB.MAINTENANCE_REPOSITORY)

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

        val application = Repositories.MariaDB.APPLICATIONS_REPOSITORY.provide().fetchByInetSocketAddress(
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

        Repositories.MariaDB.GROUPS_REPOSITORY.provide().fetchAll()
    }

    private fun preparePrimaryProviders() {
        if (!primaryPrepared) {
            primaryPrepared = true

            Databases.MariaDB.MARIA_DB_MAIN.prepare()
            Databases.Redis.REDIS_MAIN.prepare()
            Databases.Redis.REDIS_ECHO.prepare()
            Databases.Influx.INFLUX_MAIN.prepare()

            Repositories.MariaDB.SERVERS_REPOSITORY.prepare()
            Repositories.MariaDB.APPLICATIONS_REPOSITORY.prepare()

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

        object MariaDB {

            val MARIA_DB_MAIN = MariaDBDatabaseProvider(
                InetSocketAddress(
                    Env.getString("databases.maria_db.host"),
                    Env.getInt("databases.maria_db.port")
                ),
                Env.getString("databases.maria_db.user"),
                Env.getString("databases.maria_db.password"),
                Env.getString("databases.maria_db.database"),
                Env.getString("databases.maria_db.schema"),
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

        object MariaDB {

            val GROUPS_REPOSITORY = MariaDBRepositoryProvider<IGroupsRepository>(
                MariaDBGroupsRepository::class
            )

            val USERS_REPOSITORY = MariaDBRepositoryProvider<IUsersRepository>(
                MariaDBUsersRepository::class
            )

            val SERVERS_REPOSITORY = MariaDBRepositoryProvider<IServersRepository>(
                MariaDBServersRepository::class
            )

            val APPLICATIONS_REPOSITORY = MariaDBRepositoryProvider<IApplicationsRepository>(
                MariaDBApplicationsRepository::class
            )

            val USERS_GROUPS_DUE_REPOSITORY = MariaDBRepositoryProvider<IUsersGroupsDueRepository>(
                MariaDBUsersGroupsDueRepository::class
            )

            val REPORT_CATEGORIES_REPOSITORY = MariaDBRepositoryProvider<IReportCategoriesRepository>(
                MariaDBReportCategoriesRepository::class
            )

            val PUNISH_CATEGORIES_REPOSITORY = MariaDBRepositoryProvider<IPunishCategoriesRepository>(
                MariaDBPunishCategoriesRepository::class
            )

            val REVOKE_CATEGORIES_REPOSITORY = MariaDBRepositoryProvider<IRevokeCategoriesRepository>(
                MariaDBRevokeCategoriesRepository::class
            )

            val USERS_PUNISHMENTS_REPOSITORY = MariaDBRepositoryProvider<IUsersPunishmentsRepository>(
                MariaDBUsersPunishmentsRepository::class
            )

            val USERS_PASSWORDS_REPOSITORY = MariaDBRepositoryProvider<IUsersPasswordsRepository>(
                MariaDBUsersPasswordsRepository::class
            )

            val USERS_FRIENDS_REPOSITORY = MariaDBRepositoryProvider<IUsersFriendsRepository>(
                MariaDBUsersFriendsRepository::class
            )

            val IGNORED_USERS_REPOSITORY = MariaDBRepositoryProvider<IIgnoredUsersRepository>(
                MariaDBIgnoredUsersRepository::class
            )

            val USERS_PREFERENCES_REPOSITORY = MariaDBRepositoryProvider<IUsersPreferencesRepository>(
                MariaDBUsersPreferencesRepository::class
            )

            val USERS_SKINS_REPOSITORY = MariaDBRepositoryProvider<IUsersSkinsRepository>(
                MariaDBUsersSkinsRepository::class
            )

            val MAINTENANCE_REPOSITORY = MariaDBRepositoryProvider<IMaintenanceRepository>(
                MariaDBMaintenanceRepository::class
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