package net.hyren.core.shared.providers.databases.redis

import net.hyren.core.shared.providers.IProvider
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.exceptions.JedisConnectionException
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
class RedisDatabaseProvider(
    private val address: InetSocketAddress,
    private val password: String
) : IProvider<JedisPool> {

    private lateinit var _jedisPool: JedisPool

    override fun prepare() {
        try {
            val jedisPoolConfig = JedisPoolConfig()

            jedisPoolConfig.maxTotal = 8

            this._jedisPool = JedisPool(
                jedisPoolConfig,
                this.address.address.hostAddress,
                this.address.port,
                2000,
                this.password,
                0
            )
        } catch (e: JedisConnectionException) {
            e.printStackTrace()
        }
    }

    override fun provide() = this._jedisPool

    override fun shutdown() = this._jedisPool.close()

}