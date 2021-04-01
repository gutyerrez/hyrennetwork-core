package com.redefantasy.core.spigot.misc.utils

import com.redefantasy.core.spigot.CoreSpigotPlugin
import io.netty.channel.*
import net.minecraft.server.v1_8_R3.NetworkManager
import net.minecraft.server.v1_8_R3.PacketLoginInStart
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.CraftServer
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.server.PluginDisableEvent
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Gutyerrez
 */
class ProtocolHandler {

    private val ID = AtomicInteger(1)

    internal val HANDLER_NAME = "protocol-handler-${CoreSpigotPlugin.instance.name}-${ID.getAndIncrement()}"

    private lateinit var NETWORK_MANAGERS: List<NetworkManager>

    private val SERVER_CHANNELS = mutableListOf<Channel>()
    private val UNINJECTED_CHANNELS = mutableSetOf<Channel>()

    companion object {

        private val CHANNEL_LOOKUP = mutableMapOf<String, Channel>()
        private val LISTENERS = Collections.synchronizedCollection<PacketListener>(mutableListOf())

        internal fun onPacketOut(
            player: Player,
            channel: Channel,
            packet: Any?
        ): Any? {
            val event = PacketEvent(player, channel, packet)

            LISTENERS.sortedBy { it.priority }.forEach { it.onSent(event) }

            return if (event.cancelled) {
                null
            } else event.packet
        }

        internal fun onPacketIn(
            player: Player,
            channel: Channel,
            packet: Any?
        ): Any? {
            val event = PacketEvent(player, channel, packet)

            LISTENERS.sortedBy { it.priority }.forEach { it.onReceive(event) }

            return if (event.cancelled) {
                null
            } else event.packet
        }

    }

    private val SERVER_CHANNEL_HANDLER = object : ChannelInboundHandlerAdapter() {

        private val endInitProtocol = object : ChannelInitializer<Channel>() {

            override fun initChannel(channel: Channel) {
                synchronized(NETWORK_MANAGERS) {
                    if (!closed) {
                        channel.eventLoop().submit(Callable {
                            this@ProtocolHandler.injectChannelInternal(
                                channel
                            )
                        })
                    }
                }
            }

        }

        private val beginInitProtocol = object : ChannelInitializer<Channel>() {

            override fun initChannel(channel: Channel) {
                channel.pipeline().addLast(endInitProtocol)
            }

        }

        override fun channelRead(
            ctx: ChannelHandlerContext,
            message: Any
        ) {
            val channel = message as Channel

            channel.pipeline().addFirst(beginInitProtocol)

            ctx.fireChannelRead(message)
        }

    }

    private val LISTENER = object : Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        fun on(
            event: PlayerLoginEvent
        ) {
            if (closed) return

            val player = event.player

            println("entrando")

            if (player === null) return

            println("player null")

            try {
                val channel = CHANNEL_LOOKUP[player.name] ?: fun(): Channel? {
                    val craftPlayer = player as CraftPlayer
                    val handle = craftPlayer.handle
                    val playerConnection = handle.playerConnection

                    if (playerConnection === null) return null

                    val networkManager = playerConnection.networkManager

                    CHANNEL_LOOKUP[player.name] = networkManager.channel

                    return networkManager.channel
                }.invoke()

                println("Invoquei o canal")

                if (channel !== null && !UNINJECTED_CHANNELS.contains(channel)) {
                    println("Injetando")

                    this@ProtocolHandler.injectChannelInternal(channel)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @EventHandler
        fun on(
            event: PluginDisableEvent
        ) {
            val plugin = event.plugin

            if (CoreSpigotPlugin.instance.equals(plugin)) {
                this@ProtocolHandler.close()
            }
        }

    }

    private var closed: Boolean = false

    init {
        this.registerBukkitEventListener()
        this.registerChannelHandler()
    }

    private fun registerBukkitEventListener() = Bukkit.getPluginManager().registerEvents(
        LISTENER,
        CoreSpigotPlugin.instance
    )

    private fun registerChannelHandler() {
        val craftServer = Bukkit.getServer() as CraftServer
        val minecraftServer = craftServer.server
        val serverConnection = minecraftServer.serverConnection

        var looking = true

        this.NETWORK_MANAGERS = serverConnection.h

        do {
            serverConnection.g.forEach {
                val serverChannel = it.channel()

                SERVER_CHANNELS.add(serverChannel)

                serverChannel.pipeline().addFirst(SERVER_CHANNEL_HANDLER)

                looking = false
            }
        } while (looking)
    }

    fun registerListener(packetListener: PacketListener) {
        LISTENERS.add(packetListener)
    }

    private fun unregisterChannelHandler() {
        SERVER_CHANNELS.forEach {
            val channelPipeline = it.pipeline()

            it.eventLoop().execute {
                Runnable {
                    channelPipeline.remove(SERVER_CHANNEL_HANDLER)
                }
            }
        }
    }

    internal fun injectChannelInternal(channel: Channel) {
        val packetInterceptor = channel.pipeline().get(HANDLER_NAME)

        if (packetInterceptor === null) {
            channel.pipeline().addBefore(
                "packet_handler",
                HANDLER_NAME,
                PacketInterceptor()
            )

            UNINJECTED_CHANNELS.remove(channel)
        }
    }

    fun close() {
        if (!closed) {
            closed = true

            Bukkit.getServer().onlinePlayers.forEach {
                val channel = CHANNEL_LOOKUP[it.name]

                if (channel === null) return@forEach

                UNINJECTED_CHANNELS.add(channel)

                channel.eventLoop().execute {
                    Runnable {
                        channel.pipeline().remove(HANDLER_NAME)
                    }
                }
            }

            HandlerList.unregisterAll(LISTENER)

            this.unregisterChannelHandler()
        }
    }

    internal class PacketInterceptor : ChannelDuplexHandler() {

        @Volatile
        lateinit var player: Player

        override fun channelRead(
            ctx: ChannelHandlerContext,
            message: Any?
        ) {
            val channel = ctx.channel()

            this.handleLoginStart(channel, message)

            println("ler")

            onPacketIn(
                player,
                channel,
                message
            )

            if (message !== null) super.channelRead(ctx, message)
        }

        override fun write(
            ctx: ChannelHandlerContext,
            message: Any?,
            promisse: ChannelPromise?
        ) {
            println("escrever")

            onPacketOut(
                player,
                ctx.channel(),
                message
            )

            if (message !== null) super.write(ctx, message, promisse)
        }

        private fun handleLoginStart(
            channel: Channel,
            packet: Any?
        ) {
            if (packet is PacketLoginInStart) {
                val gameProfile = packet.a()

                CHANNEL_LOOKUP[gameProfile.name] = channel
            }
        }

    }

}

abstract class PacketListener(
    val priority: Int = 0
) {

    open fun onReceive(
        event: PacketEvent
    ) { /* empty */ }

    open fun onSent(
        event: PacketEvent
    ) { /* empty */ }

}

class PacketEvent(
    val player: Player,
    val channel: Channel,
    val packet: Any?,
    var cancelled: Boolean = false
)
