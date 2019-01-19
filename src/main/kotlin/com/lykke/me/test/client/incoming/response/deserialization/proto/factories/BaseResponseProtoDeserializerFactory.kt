package com.lykke.me.test.client.incoming.response.deserialization.proto.factories

import com.lykke.me.test.client.incoming.response.deserialization.ResponseDeserializer
import com.lykke.me.test.client.incoming.response.deserialization.proto.deserializers.BaseResponseProtoDeserializer

class BaseResponseProtoDeserializerFactory : ResponseProtoDeserializerFactory {
    override fun createDeserializer(): ResponseDeserializer<ByteArray> {
        return BaseResponseProtoDeserializer()
    }
}