package com.lykke.me.test.client.outgoing.messages.serialization.proto.factories

import com.lykke.me.test.client.outgoing.messages.serialization.MessageSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.ProtoMessageWrapper
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.ReservedCashInOutMessageProtoSerializer

class ReservedCashInOutMessageProtoSerializerFactory: MessageProtoSerializerFactory {
    override fun createSerializer(): MessageSerializer<ProtoMessageWrapper> {
        return ReservedCashInOutMessageProtoSerializer()
    }
}