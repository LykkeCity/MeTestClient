package com.lykke.me.test.client.socket

import com.lykke.me.test.client.AbstractMeListener
import com.lykke.me.test.client.incoming.response.Response
import com.lykke.me.test.client.incoming.response.deserialization.proto.factories.ResponseProtoDeserializerFactory
import com.lykke.me.test.client.outgoing.messages.common.MessageType
import com.lykke.me.test.utils.IntUtils
import org.apache.log4j.Logger
import java.io.DataInputStream

class MeSocketProtoResponseListener : AbstractMeListener<Response>() {

    class ResponseHandler(private val inputStream: DataInputStream,
                          private val listener: MeSocketProtoResponseListener) : Thread(ResponseHandler::class.java.name) {

        companion object {
            private val LOGGER = Logger.getLogger(ResponseHandler::class.java.name)
        }

        @Volatile
        var isRun = true

        override fun run() {
            LOGGER.debug("ResponseHandler run")
            while (isRun) {
                try {
                    readMessage()?.let {
                        listener.notifySubscribers(it)
                    }
                } catch (e: Exception) {
                    LOGGER.error(null, e)
                    isRun = false
                }
            }
        }

        private fun readMessage(): Response? {
            val byte = inputStream.readByte()
            return try {
                val bytes = readResponseByteArray()
                val messageType = MessageType.getByType(byte)
                val response = ResponseProtoDeserializerFactory.getFactory(messageType)
                        .createDeserializer()
                        .deserialize(bytes)
                LOGGER.info("Got Matching Engine response: $messageType")
                response
            } catch (e: Exception) {
                LOGGER.error("Unable to handle Matching Engine response: ${e.message}")
                null
            }
        }

        private fun readResponseByteArray(): ByteArray {
            val sizeArray = ByteArray(4)
            inputStream.readFully(sizeArray, 0, 4)
            val size = IntUtils.little2big(sizeArray)
            val serializedData = ByteArray(size)
            inputStream.readFully(serializedData, 0, size)
            return serializedData
        }
    }

    @Volatile
    private var responseHandler: ResponseHandler? = null

    fun initResponseHandler(inputStream: DataInputStream) {
        resetResponseHandler()
        val newResponseHandler = ResponseHandler(inputStream, this)
        newResponseHandler.start()
        this.responseHandler = newResponseHandler
    }

    fun resetResponseHandler() {
        val currentResponseHandler = responseHandler
        currentResponseHandler?.let {
            it.isRun = false
        }
        responseHandler = null
    }
}