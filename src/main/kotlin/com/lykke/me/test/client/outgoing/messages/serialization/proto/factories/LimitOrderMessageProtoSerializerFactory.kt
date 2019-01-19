package com.lykke.me.test.client.outgoing.messages.serialization.proto.factories

import com.lykke.me.test.client.outgoing.messages.serialization.MessageSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.LimitOrderMessageProtoSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.ProtoMessageWrapper

class LimitOrderMessageProtoSerializerFactory: MessageProtoSerializerFactory {
    override fun createSerializer(): MessageSerializer<ProtoMessageWrapper> {
        return LimitOrderMessageProtoSerializer()
    }

}