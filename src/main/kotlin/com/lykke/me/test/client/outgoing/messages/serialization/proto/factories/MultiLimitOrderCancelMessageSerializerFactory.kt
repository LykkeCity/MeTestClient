package com.lykke.me.test.client.outgoing.messages.serialization.proto.factories

import com.lykke.me.test.client.outgoing.messages.serialization.MessageSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.MultiLimitOrderCancelMessageSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.ProtoMessageWrapper

class MultiLimitOrderCancelMessageSerializerFactory: MessageProtoSerializerFactory {
    override fun createSerializer(): MessageSerializer<ProtoMessageWrapper> {
        return MultiLimitOrderCancelMessageSerializer()
    }
}