package com.redefantasy.core.shared.echo.api.misc.subscriber

import com.redefantasy.core.shared.CoreProvider
import com.redefantasy.core.shared.echo.api.Echo
import com.redefantasy.core.shared.echo.api.buffer.EchoBufferInput
import com.redefantasy.core.shared.echo.api.listener.EchoListener
import com.redefantasy.core.shared.echo.api.packets.EchoPacket
import com.redefantasy.core.shared.echo.api.packets.EchoPacketHeader
import com.redefantasy.core.shared.echo.api.response.Respondable
import com.redefantasy.core.shared.echo.api.response.Response
import org.greenrobot.eventbus.EventBus
import redis.clients.jedis.BinaryJedisPubSub
import java.util.function.BiConsumer

/**
 * @author SrGutyerrez
 **/
open class EchoSubscriber(
        private val dispatcher: BiConsumer<EchoPacket, Runnable>,
        private val echo: Echo,
) : BinaryJedisPubSub() {

    private val EVENT_BUS = EventBus.builder()
            .logNoSubscriberMessages(false)
            .logSubscriberExceptions(true)
            .throwSubscriberException(false)
            .build()

    fun callPacket(channel: String, packet: EchoPacket) {
        val clazz = packet::class.java
        val packetHeader = packet.packetHeader!!

        if (packet is Response) {
            val responseUUID = packetHeader.responseUUID

            this.echo.responseCallbacks[responseUUID]?.accept(packet)
        } else {
            this.dispatcher.accept(packet) {
                if (this.EVENT_BUS.hasSubscriberForEvent(clazz)) {
                    this.EVENT_BUS.post(packet)

                    if (packet is Respondable<*>) {
                        val responseUUID = packetHeader.responseUUID!!

                        val respondable = packet as Respondable<*>
                        val response = respondable.getResponse()

                        if (response != null) {
                            echo._publishToApplication(
                                    response,
                                    responseUUID,
                                    packetHeader.senderServerName,
                                    packetHeader.senderApplicationName
                            )
                        } else {
                            val responseType = respondable::class.java.getMethod("getResponse").returnType

                            this.echo._publishToApplication(
                                    responseType.getDeclaredConstructor().newInstance() as Response,
                                    responseUUID,
                                    packetHeader.senderServerName,
                                    packetHeader.senderApplicationName
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onMessage(channel: ByteArray, message: ByteArray) {
        val buffer = EchoBufferInput(message)

        val clazz = Class.forName(buffer.readString()) as Class<out EchoPacket>

        val packetHeader = EchoPacketHeader()

        packetHeader.read(buffer)

        if (packetHeader.senderApplicationName != null && packetHeader.senderApplicationName !== CoreProvider.application.name) {
            if (!this.isListening(clazz, packetHeader)) return

            val packet = clazz.getDeclaredConstructor().newInstance()

            packet.read(buffer)

            packet.packetHeader = packetHeader

            this.callPacket(String(channel), packet)
        }
    }

    fun registerListener(listener: EchoListener) {
        this.EVENT_BUS.register(listener)
    }

    fun isListening(clazz: Class<out EchoPacket>, packetHeader: EchoPacketHeader): Boolean {
        if (this.EVENT_BUS.hasSubscriberForEvent(clazz)) return true

        if (Response::class.java.isAssignableFrom(clazz)) {
            return if (packetHeader.responseUUID == null) {
                false
            } else {
                this.echo.responseCallbacks.containsKey(packetHeader.responseUUID)
            }
        }

        return false
    }

}