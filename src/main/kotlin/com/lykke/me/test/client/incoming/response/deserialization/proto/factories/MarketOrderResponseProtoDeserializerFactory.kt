package com.lykke.me.test.client.incoming.response.deserialization.proto.factories

import com.lykke.me.test.client.incoming.response.deserialization.ResponseDeserializer
import com.lykke.me.test.client.incoming.response.deserialization.proto.deserializers.MarketOrderResponseProtoDeserializer

class MarketOrderResponseProtoDeserializerFactory : ResponseProtoDeserializerFactory {
    override fun createDeserializer(): ResponseDeserializer<ByteArray> {
        return MarketOrderResponseProtoDeserializer()
    }
}