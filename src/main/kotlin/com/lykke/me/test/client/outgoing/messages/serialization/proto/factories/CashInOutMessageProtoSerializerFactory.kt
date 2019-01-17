package com.lykke.me.test.client.outgoing.messages.serialization.proto.factories

import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.CashInOutMessageProtoSerializer
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.MessageProtoSerializer

class CashInOutMessageProtoSerializerFactory : MessageProtoSerializerFactory {
    override fun createSerializer(): MessageProtoSerializer {
        return CashInOutMessageProtoSerializer()
    }
}