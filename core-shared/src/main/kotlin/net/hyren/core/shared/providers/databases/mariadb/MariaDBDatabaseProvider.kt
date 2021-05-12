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
        private val inetSocketAddress: InetSocketAddress,
        private val user: String,
        private val password: String,
        private val database: String,
        private val primaryDatabase: Boolean = false
) : IDatabaseProvider<Database> {

    private lateinit var _database: Database

    override fun prepare() {
        val hikariDataSource = HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = "jdbc:mysql://${inetSocketAddress.address.hostAddress}:${inetSocketAddress.port}/$database"
                driverClassName = "com.mysql.cj.jdbc.Driver"
                username = username
                password = password
                maximumPoolSize = 10

                connectionTestQuery = "SELECT 1;"

                connectionTimeout = 5000
            }
        )

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
