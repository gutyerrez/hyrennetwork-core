package com.redefantasy.core.shared.providers.databases.influx

import com.redefantasy.core.shared.providers.IProvider
import org.influxdb.BatchOptions
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
class InfluxDatabaseProvider(
        private val address: InetSocketAddress,
        private val user: String,
        private val password: String,
        private val database: String
) : IProvider<InfluxDB> {

    private lateinit var influxDB: InfluxDB

    override fun prepare() {
        val influxDB = InfluxDBFactory.connect(String.format(
                "http://${this.address.address.hostAddress}:${this.address.port}"
        ), this.user, this.password)

        influxDB.setDatabase(this.database)
        influxDB.enableBatch(BatchOptions.DEFAULTS)

        this.influxDB = influxDB
    }

    override fun provide() = this.influxDB

    override fun shutdown() {
        this.influxDB.close()
    }

}