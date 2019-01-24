package com.lykke.me.test.client.incoming.events.proto.deserialization

import com.lykke.me.test.client.incoming.events.proto.MeProtoEvent
import com.lykke.me.test.client.incoming.events.proto.ProtoCashOutEvent
import com.lykke.me.test.client.incoming.events.proto.ProtocolEvents

class ProtoCashOutEventDeserializer : MeProtoEventDeserializer<ProtocolEvents.CashOutEvent> {
    override fun deserialize(byteArray: ByteArray): MeProtoEvent<ProtocolEvents.CashOutEvent> {
        val message = ProtocolEvents.CashOutEvent.parseFrom(byteArray)
        return ProtoCashOutEvent(message, message.header.messageId)
    }

}