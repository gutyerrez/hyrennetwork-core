package net.hyren.core.shared.echo.api.misc.subscriber

import net.hyren.core.shared.echo.api.Echo
import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.core.shared.echo.api.packets.*
import net.hyren.core.shared.echo.api.response.*
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
                        val response = respondable.response

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
        println("Message size: ${message.size}")
        println("Received message: ${String(message)} on ${String(channel)}")

        val buffer = EchoBufferInput(message)

        val clazz = Class.forName(buffer.readString()) as Class<out EchoPacket>

        val packetHeader = EchoPacketHeader()

        packetHeader.read(buffer)

        if (!this.isListening(clazz, packetHeader)) {
            return
        }

        val packet = clazz.getDeclaredConstructor().newInstance()

        packet.read(buffer)

        packet.packetHeader = packetHeader

        this.callPacket(String(channel), packet)
    }

    fun registerListener(listener: EchoPacketListener) {
        this.EVENT_BUS.register(listener)
    }

    fun isListening(clazz: Class<out EchoPacket>, packetHeader: EchoPacketHeader): Boolean {
        return if (this.EVENT_BUS.hasSubscriberForEvent(clazz)) {
            true
        } else if (Response::class.java.isAssignableFrom(clazz)) {
            if (packetHeader.responseUUID == null) {
                false
            } else {
                this.echo.responseCallbacks.containsKey(packetHeader.responseUUID)
            }
        } else {
            false
        }
    }

}