package com.lykke.me.test.client.incoming.response.deserialization.proto.deserializers

import com.lykke.me.test.client.incoming.response.MessageStatus
import com.lykke.me.test.client.incoming.response.MultiLimitOrderResponse
import com.lykke.me.test.client.incoming.response.Response
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class MultiLimitOrderResponseProtoDeserializer : ResponseProtoDeserializer {
    override fun deserialize(response: ByteArray): Response {
        val protoResponse = ProtocolMessages.MultiLimitOrderResponse.parseFrom(response)

        val orderStatuses = ArrayList<MultiLimitOrderResponse.OrderStatus>(protoResponse.statusesCount)

        protoResponse.statusesList.forEach {
            orderStatuses.add(MultiLimitOrderResponse.OrderStatus(it.id,
                    if (it.hasMatchingEngineId()) it.matchingEngineId else null,
                    MessageStatus.getByType(it.status),
                    if (it.hasStatusReason()) it.statusReason else null,
                    it.volume.toBigDecimal(),
                    it.price.toBigDecimal()))
        }

        return MultiLimitOrderResponse(protoResponse.messageId,
                protoResponse.id,
                MessageStatus.getByType(protoResponse.status),
                if (protoResponse.hasStatusReason()) protoResponse.statusReason else null,
                protoResponse.assetPairId,
                orderStatuses)
    }
}