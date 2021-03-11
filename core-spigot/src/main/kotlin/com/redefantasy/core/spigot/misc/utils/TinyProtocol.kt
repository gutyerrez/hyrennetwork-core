package com.redefantasy.core.spigot.misc.utils

import com.google.common.collect.Lists
import com.google.common.collect.MapMaker
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
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Level

/**
 * @author Gutyerrez
 */
class TinyProtocol(protected var plugin: Plugin) {
    // Speedup channel lookup
    private val channelLookup: MutableMap<String, Channel> = MapMaker().weakValues().makeMap()
    private var listener: Listener? = null

    // Channels that have already been removed
    private val uninjectedChannels = Collections.newSetFromMap(MapMaker().weakKeys().makeMap<Channel, Boolean>())

    // List of network markers
    private var networkManagers: List<NetworkManager>? = null

    // Injected channel handlers
    private val serverChannels: MutableList<Channel> = Lists.newArrayList()
    private var serverChannelHandler: ChannelInboundHandlerAdapter? = null
    private var beginInitProtocol: ChannelInitializer<Channel>? = null
    private var endInitProtocol: ChannelInitializer<Channel>? = null

    // Current handler name
    private val handlerName: String
    private val listeners = Collections.synchronizedList(Lists.newArrayList<PacketListener>())

    @Volatile
    protected var closed = false
    fun registerListener(listener: PacketListener) {
        listeners.add(listener)
        listeners.sortWith { obj: PacketListener, o: PacketListener -> obj.compareTo(o) }
        plugin.logger.info("[TinyProtocol] Registered new listener: " + listeners.size)
    }

    fun unregisterListener(listener: PacketListener) {
        listeners.remove(listener)
    }

    private fun createServerChannelHandler() {
        // Handle connected channels
        endInitProtocol = object : ChannelInitializer<Channel>() {
            @Throws(Exception::class)
            override fun initChannel(channel: Channel) {
                try {
                    // This can take a while, so we need to stop the main thread from interfering
                    synchronized(networkManagers!!) {
                        // Stop injecting channels
                        if (!closed) {
                            channel.eventLoop().submit<PacketInterceptor> { injectChannelInternal(channel) }
                        }
                    }
                } catch (e: Exception) {
                    plugin.logger.log(Level.SEVERE, "Cannot inject incomming channel $channel", e)
                }
            }
        }

        // This is executed before Minecraft's channel handler
        beginInitProtocol = object : ChannelInitializer<Channel>() {
            @Throws(Exception::class)
            override fun initChannel(channel: Channel) {
                channel.pipeline().addLast(endInitProtocol)
            }
        }
        serverChannelHandler = object : ChannelInboundHandlerAdapter() {
            @Throws(Exception::class)
            override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                val channel = msg as Channel

                // Prepare to initialize ths channel
                channel.pipeline().addFirst(beginInitProtocol)
                ctx.fireChannelRead(msg)
            }
        }
    }

    /**
     * Register bukkit events.
     */
    private fun registerBukkitEvents() {
        listener = object : Listener {
            @EventHandler(priority = EventPriority.LOWEST)
            fun onPlayerLogin(e: PlayerLoginEvent) {
                if (closed) return
                if (e.player == null) {
                    return
                }
                val channel = getChannel(e.player)

                // Don't inject players that have been explicitly uninjected
                if (channel != null && !uninjectedChannels.contains(channel)) {
                    injectPlayer(e.player)
                }
            }

            @EventHandler
            fun onPluginDisable(e: PluginDisableEvent) {
                if (e.plugin == plugin) {
                    close()
                }
            }
        }
        plugin.server.pluginManager.registerEvents(listener, plugin)
    }

    private fun registerChannelHandler() {
        val mcServer = (Bukkit.getServer() as CraftServer).server
        val serverConnection = mcServer.serverConnection
        var looking = true

        // We need to synchronize against this list
        networkManagers = serverConnection.h
        createServerChannelHandler()

        // Find the correct list, or implicitly throw an exception
        var i = 0
        while (looking) {
            val list = serverConnection.g
            for (item in list) {
                // Channel future that contains the server connection
                val serverChannel = (item as ChannelFuture).channel()
                serverChannels.add(serverChannel)
                serverChannel.pipeline().addFirst(serverChannelHandler)
                looking = false
            }
            i++
        }
    }

    private fun unregisterChannelHandler() {
        if (serverChannelHandler == null) return
        for (serverChannel in serverChannels) {
            val pipeline = serverChannel.pipeline()

            // Remove channel handler
            serverChannel.eventLoop().execute {
                try {
                    pipeline.remove(serverChannelHandler)
                } catch (e: NoSuchElementException) {
                    // That's fine
                }
            }
        }
    }

    private fun registerPlayers(plugin: Plugin) {
        for (player in plugin.server.onlinePlayers) {
            injectPlayer(player)
        }
    }

    /**
     * Invoked when the server is starting to send a packet to a player.
     *
     *
     * Note that this is not executed on the main thread.
     *
     * @param receiver - the receiving player, NULL for early login/status packets.
     * @param channel  - the channel that received the packet. Never NULL.
     * @param packet   - the packet being sent.
     * @return The packet to send instead, or NULL to cancel the transmission.
     */
    fun onPacketOutAsync(receiver: Player?, channel: Channel?, packet: Any?): Any? {
        val packetEvent = PacketEvent(receiver, channel, packet, false)
        for (listener in listeners) {
            listener.onSent(packetEvent)
        }
        return if (packetEvent.isCancelled) null else packetEvent.packet
    }

    /**
     * Invoked when the server has received a packet from a given player.
     *
     *
     * Use [Channel.remoteAddress] to get the remote address of the client.
     *
     * @param sender  - the player that sent the packet, NULL for early login/status packets.
     * @param channel - channel that received the packet. Never NULL.
     * @param packet  - the packet being received.
     * @return The packet to recieve instead, or NULL to cancel.
     */
    fun onPacketInAsync(sender: Player?, channel: Channel?, packet: Any?): Any? {
        val packetEvent = PacketEvent(sender, channel, packet, false)

        for (listener in listeners) {
            listener.onReceive(packetEvent)
        }

        return if (packetEvent.isCancelled) null else packetEvent.packet
    }

    /**
     * Send a packet to a particular player.
     *
     *
     * Note that [.onPacketOutAsync] will be invoked with this packet.
     *
     * @param player - the destination player.
     * @param packet - the packet to send.
     */
    fun sendPacket(player: Player, packet: Any?) {
        sendPacket(getChannel(player), packet)
    }

    /**
     * Send a packet to a particular client.
     *
     *
     * Note that [.onPacketOutAsync] will be invoked with this packet.
     *
     * @param channel - client identified by a channel.
     * @param packet  - the packet to send.
     */
    fun sendPacket(channel: Channel?, packet: Any?) {
        channel!!.pipeline().writeAndFlush(packet)
    }

    /**
     * Pretend that a given packet has been received from a player.
     *
     *
     * Note that [.onPacketInAsync] will be invoked with this packet.
     *
     * @param player - the player that sent the packet.
     * @param packet - the packet that will be received by the server.
     */
    fun receivePacket(player: Player, packet: Any?) {
        receivePacket(getChannel(player), packet)
    }

    /**
     * Pretend that a given packet has been received from a given client.
     *
     *
     * Note that [.onPacketInAsync] will be invoked with this packet.
     *
     * @param channel - client identified by a channel.
     * @param packet  - the packet that will be received by the server.
     */
    fun receivePacket(channel: Channel?, packet: Any?) {
        channel!!.pipeline().context("encoder").fireChannelRead(packet)
    }

    /**
     * Retrieve the name of the channel injector, default implementation is "tiny-" + plugin name + "-" + a unique ID.
     *
     *
     * Note that this method will only be invoked once. It is no longer necessary to override this to support multiple instances.
     *
     * @return A unique channel handler name.
     */
    protected fun getHandlerName(): String {
        return "tiny-" + plugin.name + "-" + ID.incrementAndGet()
    }

    /**
     * Add a custom channel handler to the given player's channel pipeline, allowing us to intercept sent and received packets.
     *
     *
     * This will automatically be called when a player has logged in.
     *
     * @param player - the player to inject.
     */
    fun injectPlayer(player: Player) {
        injectChannelInternal(getChannel(player)).player = player
    }

    /**
     * Add a custom channel handler to the given channel.
     *
     * @param channel - the channel to inject.
     * @return The intercepted channel, or NULL if it has already been injected.
     */
    fun injectChannel(channel: Channel?) {
        injectChannelInternal(channel)
    }

    /**
     * Add a custom channel handler to the given channel.
     *
     * @param channel - the channel to inject.
     * @return The packet interceptor.
     */
    private fun injectChannelInternal(channel: Channel?): PacketInterceptor {
        return try {
            var interceptor = channel!!.pipeline()[handlerName] as PacketInterceptor

            // Inject our packet interceptor
            if (interceptor == null) {
                interceptor = PacketInterceptor()
                channel.pipeline().addBefore("packet_handler", handlerName, interceptor)
                uninjectedChannels.remove(channel)
            }
            interceptor
        } catch (e: IllegalArgumentException) {
            // Try again
            channel!!.pipeline()[handlerName] as PacketInterceptor
        }
    }

    /**
     * Retrieve the Netty channel associated with a player. This is cached.
     *
     * @param player - the player.
     * @return The Netty channel.
     */
    fun getChannel(player: Player): Channel? {
        var channel = channelLookup[player.name]

        // Lookup channel again
        if (channel == null) {
            val connection = (player as CraftPlayer).handle.playerConnection ?: return null
            val manager = connection.networkManager
            channelLookup[player.getName()] = manager.channel.also { channel = it }
        }
        return channel
    }

    /**
     * Uninject a specific player.
     *
     * @param player - the injected player.
     */
    fun uninjectPlayer(player: Player) {
        uninjectChannel(getChannel(player))
    }

    /**
     * Uninject a specific channel.
     *
     *
     * This will also disable the automatic channel injection that occurs when a player has properly logged in.
     *
     * @param channel - the injected channel.
     */
    fun uninjectChannel(channel: Channel?) {
        // No need to guard against this if we're closing
        if (!closed) {
            uninjectedChannels.add(channel)
        }

        // See ChannelInjector in ProtocolLib, line 590
        channel!!.eventLoop().execute { channel.pipeline().remove(handlerName) }
    }

    /**
     * Determine if the given player has been injected by TinyProtocol.
     *
     * @param player - the player.
     * @return TRUE if it is, FALSE otherwise.
     */
    fun hasInjected(player: Player): Boolean {
        return hasInjected(getChannel(player))
    }

    /**
     * Determine if the given channel has been injected by TinyProtocol.
     *
     * @param channel - the channel.
     * @return TRUE if it is, FALSE otherwise.
     */
    fun hasInjected(channel: Channel?): Boolean {
        return channel!!.pipeline()[handlerName] != null
    }

    /**
     * Cease listening for packets. This is called automatically when your plugin is disabled.
     */
    fun close() {
        if (!closed) {
            closed = true

            // Remove our handlers
            for (player in plugin.server.onlinePlayers) {
                uninjectPlayer(player)
            }

            // Clean up Bukkit
            HandlerList.unregisterAll(listener)
            unregisterChannelHandler()
        }
    }

    open class PacketListener(
        private val priority: Int = 0
    ) : Comparable<PacketListener> {

        open fun onReceive(event: PacketEvent) {
            //
        }

        open fun onSent(event: PacketEvent) {
            //
        }

        override fun compareTo(packetListener: PacketListener) = priority.compareTo(packetListener.priority)

    }

    class PacketEvent(
        val player: Player? = null,
        val channel: Channel? = null,
        val packet: Any? = null,
        val isCancelled: Boolean = false,
    )

    /**
     * Channel handler that is inserted into the player's channel pipeline, allowing us to intercept sent and received packets.
     *
     * @author Kristian
     */
    private inner class PacketInterceptor : ChannelDuplexHandler() {
        // Updated by the login event
        @Volatile
        var player: Player? = null

        @Throws(Exception::class)
        override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
            val channel = ctx.channel()
            var msg: Any? = msg

            handleLoginStart(channel, msg)

            try {
                msg = onPacketInAsync(player, channel, msg)
            } catch (e: Exception) {
                plugin.logger.log(Level.SEVERE, "Error in onPacketInAsync().", e)
            }

            if (msg != null) {
                super.channelRead(ctx, msg)
            }
        }

        @Throws(Exception::class)
        override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
            var msg: Any? = msg
            try {
                msg = onPacketOutAsync(player, ctx.channel(), msg)
            } catch (e: Exception) {
                plugin.logger.log(Level.SEVERE, "Error in onPacketOutAsync().", e)
            }
            if (msg != null) {
                super.write(ctx, msg, promise)
            }
        }

        private fun handleLoginStart(channel: Channel, packet: Any?) {
            if (packet is PacketLoginInStart) {
                val profile = packet.a()
                channelLookup[profile.name] = channel
            }
        }
    }

    companion object {
        private val ID = AtomicInteger(0)
    }

    /**
     * Construct a new instance of TinyProtocol, and start intercepting packets for all connected clients and future clients.
     *
     *
     * You can construct multiple instances per plugin.
     *
     * @param plugin - the plugin.
     */
    init {
        handlerName = getHandlerName()

        registerBukkitEvents()

        try {
            registerChannelHandler()
            registerPlayers(plugin)
            plugin.logger.info("[TinyProtocol] Initialized")
        } catch (ex: IllegalArgumentException) {
            // Damn you, late bind
            plugin.logger.info("[TinyProtocol] Delaying server channel injection due to late bind.")
            object : BukkitRunnable() {
                override fun run() {
                    registerChannelHandler()
                    registerPlayers(plugin)
                    plugin.logger.info("[TinyProtocol] Late bind injection successful.")
                }
            }.runTask(plugin)
        }
    }
}