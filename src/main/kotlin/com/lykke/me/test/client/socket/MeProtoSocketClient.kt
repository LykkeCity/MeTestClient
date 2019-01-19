package com.lykke.me.test.client.socket

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.common.MessageType
import com.lykke.me.test.client.outgoing.messages.serialization.proto.factories.MessageProtoSerializerFactory
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.ProtoMessageWrapper
import org.apache.log4j.Logger
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue

class MeProtoSocketClient(private val responseListener: MeProtoSocketResponseListener,
                          private val host: String,
                          private val port: Int)
    : MeClient, Thread(MeProtoSocketClient::class.java.name) {

    companion object {
        private val LOGGER = Logger.getLogger(MeProtoSocketClient::class.java.name)
        private const val DELAY = 1000L
    }

    private var socket: Socket? = null
    private var outputStream: DataOutputStream? = null
    private var inputStream: DataInputStream? = null
    private var connected = false
    private val messagesQueue = LinkedBlockingQueue<ProtoMessageWrapper>()

    override fun sendMessage(message: Message) {
        messagesQueue.put(MessageProtoSerializerFactory
                .getFactory(message.getType())
                .createSerializer()
                .serialize(message))
    }

    override fun run() {
        while (true) {
            try {
                waitConnecting()
                val message = messagesQueue.take()
                try {
                    if (connected) {
                        sendMessage(message)
                    }
                } catch (e: Throwable) {
                    try {
                        closeConnection()
                        waitConnecting()
                        sendMessage(message)
                    } catch (e: Throwable) {
                        LOGGER.error(null, e)
                        closeConnection()
                    }
                }
            } catch (e: Throwable) {
                LOGGER.error(null, e)
                Thread.sleep(DELAY)
            }
        }
    }

    private fun waitConnecting() {
        while (!connected) {
            connect()
        }
    }

    private fun connect() {
        LOGGER.info("Connecting to Matching Engine")
        socket = Socket(host, port)
        while (!socket!!.isConnected) {
            LOGGER.info("Waiting socket server")
            Thread.sleep(DELAY)
        }
        outputStream = DataOutputStream(BufferedOutputStream(socket!!.getOutputStream()))
        inputStream = DataInputStream(BufferedInputStream(socket!!.getInputStream()))

        outputStream!!.flush()
        outputStream!!.write(byteArrayOf(MessageType.PING.type))
        outputStream!!.flush()

        val type = inputStream!!.readByte()
        if (type == MessageType.PING.type) {
            responseListener.initResponseHandler(inputStream!!)
            connected = true
            LOGGER.info("Connected to Matching Engine")
        } else {
            LOGGER.error("PING response is incorrect")
            Thread.sleep(DELAY)
        }
    }

    private fun closeConnection() {
        try {
            responseListener.resetResponseHandler()
            connected = false
            inputStream?.close()
            outputStream?.close()
            socket?.close()
        } catch (e: Throwable) {
            // ignored
        }
    }

    private fun sendMessage(message: ProtoMessageWrapper) {
        outputStream!!.write(toByteArray(message.messageType.type,
                message.generatedMessage.serializedSize,
                message.generatedMessage.toByteArray()))
        outputStream!!.flush()
    }

    private fun toByteArray(type: Byte, size: Int, data: ByteArray): ByteArray {
        val result = ByteArray(5 + data.size)
        result[0] = type
        result[1] = size.toByte()
        result[2] = size.ushr(8).toByte()
        result[3] = size.ushr(16).toByte()
        result[4] = size.ushr(24).toByte()
        System.arraycopy(data, 0, result, 5, data.size)
        return result
    }
}