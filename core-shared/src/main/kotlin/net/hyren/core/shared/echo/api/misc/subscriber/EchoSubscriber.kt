package net.hyren.core.shared.echo.api.misc.subscriber

import net.hyren.core.shared.echo.api.Echo
import net.hyren.core.shared.echo.api.buffer.EchoBufferInput
import net.hyren.core.shared.echo.api.listener.EchoPacketListener
import net.hyren.core.shared.echo.api.packets.*
import net.hyren.core.shared.echo.api.response.*
import org.greenrobot.eventbus.EventBus
import redis.clients.jedis.BinaryJedisPubSub
import java.util.function.BiConsumer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

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

    fun callPacket(
        channel: String,
        packet: EchoPacket
    ) {
        val kClass = packet::class
        val packetHeader = packet.packetHeader!!

        if (packet is Response) {
            val responseUUID = packetHeader.responseUUID

            echo.responseCallbacks[responseUUID]?.accept(packet)
        } else {
            dispatcher.accept(packet) {
                if (EVENT_BUS.hasSubscriberForEvent(kClass.java)) {
                    EVENT_BUS.post(packet)

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

                            echo._publishToApplication(
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

    override fun onMessage(
        channel: ByteArray,
        message: ByteArray
    ) {
        val buffer = EchoBufferInput(message)

        val kClass = (Class.forName(buffer.readString()) as Class<EchoPacket>).kotlin

        val packetHeader = EchoPacketHeader()

        packetHeader.read(buffer)

        if (isListening(kClass, packetHeader)) {
            val packet = kClass.primaryConstructor!!.call()

            packet.read(buffer)

            packet.packetHeader = packetHeader

            callPacket(String(channel), packet)
        }
    }

    fun registerListener(
        listener: EchoPacketListener
    ) = EVENT_BUS.register(listener)

    private fun isListening(
        kClass: KClass<out EchoPacket>,
        packetHeader: EchoPacketHeader
    ) = when {
        EVENT_BUS.hasSubscriberForEvent(kClass.java) -> true
        Response::class == kClass -> echo.responseCallbacks.containsKey(packetHeader.responseUUID)
        else -> false
    }

}