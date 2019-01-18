package com.lykke.me.test.client.incoming.response.deserialization.proto.factories

import com.lykke.me.test.client.incoming.response.deserialization.ResponseDeserializer
import com.lykke.me.test.client.incoming.response.deserialization.proto.deserializers.MultiLimitOrderResponseProtoDeserializer

class MultiLimitOrderResponseProtoDeserializerFactory : ResponseProtoDeserializerFactory {
    override fun createDeserializer(): ResponseDeserializer<ByteArray> {
        return MultiLimitOrderResponseProtoDeserializer()
    }
}