package com.lykke.me.test.client.incoming.response.deserialization.proto.factories

import com.lykke.me.test.client.incoming.response.deserialization.ResponseDeserializerFactory
import com.lykke.me.test.client.outgoing.messages.common.MessageType

interface ResponseProtoDeserializerFactory : ResponseDeserializerFactory<ByteArray> {

    companion object {
        private val deserializerFactoryByMessageType = mapOf(
                Pair(MessageType.NEW_RESPONSE, BaseResponseProtoDeserializerFactory()),
                Pair(MessageType.MARKET_ORDER_RESPONSE, MarketOrderResponseProtoDeserializerFactory()),
                Pair(MessageType.MULTI_LIMIT_ORDER_RESPONSE, MultiLimitOrderResponseProtoDeserializerFactory())
        )

        fun getFactory(messageType: MessageType): ResponseProtoDeserializerFactory {
            return deserializerFactoryByMessageType[messageType]
                    ?: throw IllegalArgumentException("Unsupported message type: $messageType")
        }
    }
}