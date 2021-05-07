package net.hyren.core.shared.providers.databases.redis.echo.providers

import net.hyren.core.shared.echo.api.Echo
import net.hyren.core.shared.providers.IProvider
import net.hyren.core.shared.providers.databases.redis.RedisDatabaseProvider

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