package net.hyren.core.shared.providers.databases.postgres

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.hyren.core.shared.providers.databases.IDatabaseProvider
import org.jetbrains.exposed.sql.Database
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
class PostgresDatabaseProvider(
        private val address: InetSocketAddress,
        private val user: String,
        private val password: String,
        private val database: String,
        private val schema: String
) : IDatabaseProvider<Unit> {

    private lateinit var hikariDataSource: HikariDataSource

    override fun prepare() {
        val hikariConfig = HikariConfig()

        // setup datasource
        hikariConfig.dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"

        // setup data properties
        hikariConfig.addDataSourceProperty("serverName", this.address.address.hostAddress)
        hikariConfig.addDataSourceProperty("portNumber", this.address.port)
        hikariConfig.addDataSourceProperty("databaseName", this.database)
        hikariConfig.addDataSourceProperty("user", this.user)
        hikariConfig.addDataSourceProperty("password", this.password)

        hikariConfig.schema = this.schema

        hikariConfig.connectionTestQuery = "SELECT 1;"

        hikariConfig.maximumPoolSize = 1
        hikariConfig.connectionTimeout = 5000

        this.hikariDataSource = HikariDataSource(hikariConfig)

        Database.connect(this.hikariDataSource)
    }

    override fun provide() { /* not implemented method */ }

    override fun shutdown() {
        this.hikariDataSource.close()
    }

}