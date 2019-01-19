package com.lykke.me.test.client.outgoing.messages.serialization.proto.factories

import com.lykke.me.test.client.outgoing.messages.serialization.MessageSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.MarketOrderMessageProtoSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.ProtoMessageWrapper

class MarketOrderMessageProtoSerializerFactory: MessageProtoSerializerFactory {
    override fun createSerializer(): MessageSerializer<ProtoMessageWrapper> {
        return MarketOrderMessageProtoSerializer()
    }
}