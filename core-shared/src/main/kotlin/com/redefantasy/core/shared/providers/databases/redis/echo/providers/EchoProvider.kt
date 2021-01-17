package com.redefantasy.core.shared.providers.databases.redis.echo.providers

import com.redefantasy.core.shared.echo.api.Echo
import com.redefantasy.core.shared.providers.IProvider
import com.redefantasy.core.shared.providers.databases.redis.RedisDatabaseProvider

/**
 * @author SrGutyerrez
 **/
class EchoProvider(
        private val redisDatabaseProvider: RedisDatabaseProvider
) : IProvider<Echo> {

    private lateinit var echo: Echo

    override fun prepare() {
        this.echo = Echo(this.redisDatabaseProvider)
    }

    override fun provide() = this.echo

}