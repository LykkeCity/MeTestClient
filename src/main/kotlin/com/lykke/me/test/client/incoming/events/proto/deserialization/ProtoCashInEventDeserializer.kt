package com.lykke.me.test.client.incoming.events.proto.deserialization

import com.lykke.me.test.client.incoming.events.proto.MeProtoEvent
import com.lykke.me.test.client.incoming.events.proto.ProtoCashInEvent
import com.lykke.me.test.client.incoming.events.proto.ProtocolEvents

class ProtoCashInEventDeserializer : MeProtoEventDeserializer<ProtocolEvents.CashInEvent> {
    override fun deserialize(byteArray: ByteArray): MeProtoEvent<ProtocolEvents.CashInEvent> {
        val message = ProtocolEvents.CashInEvent.parseFrom(byteArray)
        return ProtoCashInEvent(message, message.header.messageId)
    }
}