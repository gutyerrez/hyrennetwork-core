package net.hyren.core.shared.echo.api

import net.hyren.core.shared.CoreProvider
import net.hyren.core.shared.applications.ApplicationType
import net.hyren.core.shared.applications.data.Application
import net.hyren.core.shared.echo.api.buffer.EchoBufferOutput
import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.core.shared.echo.api.misc.subscriber.EchoSubscriber
import net.hyren.core.shared.echo.api.packets.*
import net.hyren.core.shared.echo.api.response.*
import net.hyren.core.shared.providers.databases.redis.RedisDatabaseProvider
import net.hyren.core.shared.servers.data.Server
import redis.clients.jedis.util.SafeEncoder
import java.util.*
import java.util.function.*

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

        val source = CoreProvider.application

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

    fun <T : EchoPacket> publishToApplications(
        packet: T,
        targetApplications: Array<Application?>
    ) {
        targetApplications.forEach {
            if (it !== null) this.publishToApplication(
                packet,
                it.server?.getName(),
                it.name
            )
        }
    }

    fun <T : EchoPacket> publishToApplications(
        packet: T,
        targetApplications: Collection<Application?>
    ) {
        targetApplications.forEach {
            if (it !== null) this.publishToApplication(
                packet,
                it.server?.getName(),
                it.name
            )
        }
    }

    fun <T : EchoPacket> publishToAll(packet: T) {
        this._publish(
            packet,
            this.createHeader(null),
            this.BASE_CHANNEL_NAME
        )
    }

    fun <T : EchoPacket> publishToApplicationType(
        packet: T,
        applicationType: ApplicationType
    ) {
        this._publish(
            packet,
            this.createHeader(null),
            String.format(
                this.APPLICATION_TYPE_CHANNEL_NAME,
                applicationType.name
            )
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
            CoreProvider.application.server
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

    fun <R : Response, T> publishToCurrentServer(
        packet: T,
        onResponse: Consumer<R>
    ) where T : EchoPacket, T : Respondable<R> {
        val responseUUID = UUID.randomUUID()

        this.responseCallbacks[responseUUID] = onResponse as Consumer<Response>

        this._publish(
            packet,
            this.createHeader(
                responseUUID
            ),
            String.format(
                this.SERVER_CHANNEL_NAME,
                CoreProvider.application.server?.name
            )
        )
    }

    fun <R : Response, T> publishToServer(
        packet: T,
        targetServerName: String?,
        onResponse: Consumer<R>
    ) where T : EchoPacket, T : Respondable<R> {
        val responseUUID = UUID.randomUUID()

        this.responseCallbacks[responseUUID] = onResponse as Consumer<Response>

        this._publish(
            packet,
            this.createHeader(
                responseUUID
            ),
            String.format(
                this.SERVER_CHANNEL_NAME,
                targetServerName
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

        this.redisDatabaseProvider.provide().resource.use {
            it.publish(channel.toByteArray(), buffer.toByteArray())
        }
    }

    fun subscribe(dispatcher: BiConsumer<EchoPacket, Runnable>? = null): EchoSubscriber {
        val channels = mutableListOf(this.BASE_CHANNEL_NAME)

        val source = CoreProvider.application

        channels.add(
            String.format(
                this.APPLICATION_CHANNEL_NAME,
                source.server?.name ?: "null",
                source.name
            )
        )
        channels.add(
            String.format(
                this.APPLICATION_TYPE_CHANNEL_NAME,
                source.applicationType.name
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

    fun registerListener(listener: EchoPacketListener) {
        if (this.defaultSubscriber == null) {
            throw RuntimeException("Error while registering echo listener, Default subscriber is null")
        }

        this.defaultSubscriber!!.registerListener(listener)
    }

}