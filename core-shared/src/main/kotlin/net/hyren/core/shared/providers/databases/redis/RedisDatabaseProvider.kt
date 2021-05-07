package net.hyren.core.shared.providers.databases.redis

import net.hyren.core.shared.providers.IProvider
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import java.net.InetSocketAddress

/**
 * @author SrGutyerrez
 **/
class RedisDatabaseProvider(
        private val address: InetSocketAddress,
        private val password: String
) : IProvider<JedisPool> {

    private lateinit var jedisPool: JedisPool

    override fun prepare() {
        val jedisPoolConfig = JedisPoolConfig()

        jedisPoolConfig.maxTotal = 8

        val jedisPool = JedisPool(
                jedisPoolConfig,
                this.address.address.hostAddress,
                this.address.port,
                2000,
                this.password,
                0
        )

        this.jedisPool = jedisPool
    }

    override fun provide() = this.jedisPool

    override fun shutdown() {
        this.jedisPool.close()
    }

}