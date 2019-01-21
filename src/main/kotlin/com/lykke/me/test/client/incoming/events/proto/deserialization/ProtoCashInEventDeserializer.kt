package com.lykke.me.test.client.incoming.events.proto.deserialization

import com.lykke.me.test.client.incoming.events.proto.MeProtoEvent
import com.lykke.me.test.client.incoming.events.proto.ProtoCashInEvent
import com.lykke.me.test.client.incoming.events.proto.ProtocolEvents

class ProtoCashInEventDeserializer : MeProtoEventDeserializer<ProtocolEvents.CashInEvent> {
    override fun deserialize(byteArray: ByteArray): MeProtoEvent<ProtocolEvents.CashInEvent> {
        return ProtoCashInEvent(ProtocolEvents.CashInEvent.parseFrom(byteArray))
    }
}