package net.hyren.core.shared.providers.databases.mariadb

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.hyren.core.shared.providers.databases.IDatabaseProvider
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
class MariaDBDatabaseProvider(
        private val address: InetSocketAddress,
        private val user: String,
        private val password: String,
        private val database: String,
        private val schema: String,
        private val primaryDatabase: Boolean = false
) : IDatabaseProvider<Database> {

    private lateinit var _database: Database

    override fun prepare() {
        val hikariConfig = HikariConfig()

        // setup driver
        hikariConfig.driverClassName = "com.mysql.cj.jdbc.Driver"

        // setup datasource
        hikariConfig.dataSourceClassName = "org.mariadb.jdbc.MariaDbDataSource"

        // setup data properties
        hikariConfig.addDataSourceProperty("serverName", this.address.address.hostAddress)
        hikariConfig.addDataSourceProperty("portNumber", this.address.port)
        hikariConfig.addDataSourceProperty("databaseName", this.database)
        hikariConfig.addDataSourceProperty("user", this.user)
        hikariConfig.addDataSourceProperty("password", this.password)

        hikariConfig.connectionTestQuery = "SELECT 1;"

        hikariConfig.maximumPoolSize = 10
        hikariConfig.connectionTimeout = 5000

        val hikariDataSource = HikariDataSource(hikariConfig)

        this._database = Database.connect(hikariDataSource)

        if (primaryDatabase) {
            TransactionManager.defaultDatabase = _database
        }
    }

    override fun provide() = _database

    override fun shutdown() {
        TransactionManager.closeAndUnregister(_database)
    }

}
