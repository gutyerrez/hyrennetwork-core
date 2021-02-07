package com.redefantasy.core.shared.echo.api

import com.redefantasy.core.shared.applications.ApplicationType
import com.redefantasy.core.shared.applications.data.Application
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferOutput
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.api.misc.subscriber.EchoSubscriber
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.echo.api.packets.EchoPacketHeader
import com.redefantasy.core.shared.echo.api.response.Respondable
import com.redefantasy.core.shared.echo.api.response.Response
import com.redefantasy.core.shared.providers.databases.redis.RedisDatabaseProvider
import com.redefantasy.core.shared.servers.data.Server
import redis.clients.jedis.util.SafeEncoder
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * @author SrGutyerrez
 **/
open class Echo(
        private val redisDatabaseProvider: RedisDatabaseProvider
) {

    private val BASE_CHANNEL_NAME = "minecraft/"
    private val APPLICATION_TYPE_CHANNEL_NAME = "minecraft/application-type/%s"
    private val SERVER_CHANNEL_NAME = "minecraft/server/%s"
    private val SERVER_APPLICATION_TYPE_CHANNEL_NAME = "minecraft/server/%s/application-type/%s"
    private val APPLICATION_CHANNEL_NAME = "minecraft/server/%s/%s"

    private val subscribers = mutableListOf<EchoSubscriber>()

    val responseCallbacks = mutableMapOf<UUID, Consumer<Response>>()

    var defaultSubscriber: EchoSubscriber? = null

    private fun createHeader(responseUUID: UUID?): EchoPacketHeader {
        val uuid = UUID.randomUUID()

        val source = com.redefantasy.core.shared.CoreProvider.application

        return EchoPacketHeader(
                uuid,
                source.name,
                source.displayName,
                responseUUID
        )
    }

    fun <T : EchoPacket> _publishToApplication(
            packet: T,
            responseUUID: UUID,
            targetServerName: String?,
            targetApplicationName: String?
    ) {
        this._publish(
                packet,
                this.createHeader(
                        responseUUID
                ),
                String.format(
                        this.APPLICATION_CHANNEL_NAME,
                        targetServerName,
                        targetApplicationName
                )
        )
    }

    fun <T : EchoPacket> publishToApplication(
            packet: T,
            targetServerName: String?,
            targetApplicationName: String?
    ) {
        this._publish(
                packet,
                this.createHeader(null),
                String.format(
                        this.APPLICATION_CHANNEL_NAME,
                        targetServerName,
                        targetApplicationName
                )
        )
    }

    fun <R : Response, T> publishToApplication(
            packet: T,
            targetServerName: String?,
            targetApplicationName: String?,
            onResponse: Consumer<R>
    ) where T : EchoPacket, T : Respondable<R> {
        val responseUUID = UUID.randomUUID()

        this.responseCallbacks[responseUUID] = onResponse as Consumer<Response>

        this._publishToApplication(
                packet,
                responseUUID,
                targetServerName,
                targetApplicationName
        )
    }

    fun <R : Response, T> publishToApplication(
            packet: T,
            targetApplication: Application
    ) where T : EchoPacket, T : Respondable<R> {
        this.publishToApplication(
                packet,
                targetApplication.server?.getName(),
                targetApplication.name
        )
    }

    fun <R : Response, T> publishToApplication(
            packet: T,
            targetApplication: Application,
            onResponse: Consumer<R>
    ) where T : EchoPacket, T : Respondable<R> {
        this.publishToApplication(
                packet,
                targetApplication.server?.getName(),
                targetApplication.name,
                onResponse
        )
    }

    fun <T : EchoPacket> publishToAll(packet: T) {
        this._publish(
                packet,
                this.createHeader(null),
                this.BASE_CHANNEL_NAME
        )
    }

    fun <T : EchoPacket> publishToApplicationTypeAndServer(
            packet: T,
            server: Server,
            applicationType: ApplicationType
    ) {
        this._publish(
                packet,
                this.createHeader(null),
                String.format(
                        this.SERVER_APPLICATION_TYPE_CHANNEL_NAME,
                        server.name,
                        applicationType.name
                )
        )
    }

    fun <T : EchoPacket> publishToCurrentServer(packet: T) {
        this.publishToServer(
                packet,
                com.redefantasy.core.shared.CoreProvider.application.server
        )
    }

    fun <T : EchoPacket> publishToServer(packet: T, server: Server?) {
        this._publish(
                packet,
                this.createHeader(null),
                String.format(
                        this.SERVER_CHANNEL_NAME,
                        server?.name
                )
        )
    }

    fun <T : EchoPacket> _publish(
            packet: T,
            packetHeader: EchoPacketHeader,
            channel: String
    ) {
        val clazz = packet::class.java

        val buffer = EchoBufferOutput()

        buffer.writeString(clazz.name)

        packetHeader.write(buffer)
        packet.write(buffer)

        packet.packetHeader = packetHeader

        this.subscribers.forEach { it.callPacket(channel, packet) }

        val jedis = this.redisDatabaseProvider.provide().resource

        jedis.publish(channel.toByteArray(), buffer.toByteArray())
    }

    fun subscribe(dispatcher: BiConsumer<EchoPacket, Runnable>? = null): EchoSubscriber {
        val channels = mutableListOf(this.BASE_CHANNEL_NAME)

        val source = com.redefantasy.core.shared.CoreProvider.application

        channels.add(
                String.format(
                        this.APPLICATION_CHANNEL_NAME,
                        source.server?.name ?: "undefined",
                        source.name
                )
        )
        channels.add(
                String.format(
                        this.APPLICATION_TYPE_CHANNEL_NAME,
                        source.name
                )
        )

        if (source.server != null) {
            channels.add(
                    String.format(
                            this.SERVER_CHANNEL_NAME,
                            source.server.name
                    )
            )
            channels.add(
                    String.format(
                            this.SERVER_APPLICATION_TYPE_CHANNEL_NAME,
                            source.server.name,
                            source.applicationType.name
                    )
            )
        }

        val echoSubscriber: EchoSubscriber

        if (dispatcher == null) {
            echoSubscriber = EchoSubscriber({ _, runnable ->
                runnable.run()
            }, this)
        } else {
            echoSubscriber = EchoSubscriber(dispatcher, this)
        }

        Thread({
            this.redisDatabaseProvider.provide().resource.use {
                it.subscribe(
                        echoSubscriber,
                        *SafeEncoder.encodeMany(
                                *channels.toTypedArray()
                        )
                )
            }
        }, "Echo Subscriber Thread").start()

        return echoSubscriber
    }

    fun registerListener(listener: EchoListener) {
        if (this.defaultSubscriber == null) {
            throw RuntimeException("Error while registering echo listener, Default subscriber is null")
        }

        this.defaultSubscriber!!.registerListener(listener)
    }

}