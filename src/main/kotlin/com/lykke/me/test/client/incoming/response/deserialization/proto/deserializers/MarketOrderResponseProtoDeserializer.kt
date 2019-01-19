package com.lykke.me.test.client.incoming.response.deserialization.proto.deserializers

import com.lykke.me.test.client.incoming.response.MarketOrderResponse
import com.lykke.me.test.client.incoming.response.MessageStatus
import com.lykke.me.test.client.incoming.response.Response
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class MarketOrderResponseProtoDeserializer : ResponseProtoDeserializer {
    override fun deserialize(response: ByteArray): Response {
        val protoResponse = ProtocolMessages.MarketOrderResponse.parseFrom(response)
        return MarketOrderResponse(protoResponse.id,
                protoResponse.messageId,
                MessageStatus.getByType(protoResponse.status),
                if (protoResponse.hasStatusReason()) protoResponse.statusReason else null,
                if (protoResponse.hasPrice()) protoResponse.price.toBigDecimal() else null)
    }
}