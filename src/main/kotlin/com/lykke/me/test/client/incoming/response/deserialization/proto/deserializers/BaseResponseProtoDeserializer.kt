package com.lykke.me.test.client.incoming.response.deserialization.proto.deserializers

import com.lykke.me.test.client.incoming.response.BaseResponse
import com.lykke.me.test.client.incoming.response.MessageStatus
import com.lykke.me.test.client.incoming.response.Response
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class BaseResponseProtoDeserializer : ResponseProtoDeserializer {
    override fun deserialize(response: ByteArray): Response {
        val protoResponse = ProtocolMessages.NewResponse.parseFrom(response)
        return BaseResponse(protoResponse.id,
                protoResponse.messageId,
                if (protoResponse.hasMatchingEngineId()) protoResponse.matchingEngineId else null,
                MessageStatus.getByType(protoResponse.status),
                if (protoResponse.hasStatusReason()) protoResponse.statusReason else null)
    }
}