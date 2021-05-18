package net.hyren.core.shared.providers.databases.postgresql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.hyren.core.shared.providers.databases.IDatabaseProvider
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
class PostgreSQLDatabaseProvider(
        private val inetSocketAddress: InetSocketAddress,
        private val user: String,
        private val password: String,
        private val database: String,
        private val schema: String,
        private val primaryDatabase: Boolean = false
) : IDatabaseProvider<Database> {

    private lateinit var _database: Database

    override fun prepare() {
        _database = Database.connect(HikariDataSource(
            HikariConfig().apply {
                dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"

                dataSourceProperties["serverName"] = this@PostgreSQLDatabaseProvider.inetSocketAddress.address.hostAddress
                dataSourceProperties["portNumber"] = this@PostgreSQLDatabaseProvider.inetSocketAddress.port
                dataSourceProperties["databaseName"] = this@PostgreSQLDatabaseProvider.database
                dataSourceProperties["user"] = this@PostgreSQLDatabaseProvider.user
                dataSourceProperties["password"] = this@PostgreSQLDatabaseProvider.password

                maximumPoolSize = 10
                connectionTimeout = 5000

                schema = this@PostgreSQLDatabaseProvider.schema

                connectionTestQuery = "SELECT 1;"
            }
        ))

        if (primaryDatabase) {
            TransactionManager.defaultDatabase = _database
        }
    }

    override fun provide() = _database

    override fun shutdown() {
        TransactionManager.closeAndUnregister(_database)
    }

}
