package com.lykke.me.test.client.incoming.events.proto.deserialization

import com.lykke.me.test.client.incoming.events.proto.MeProtoEvent
import com.lykke.me.test.client.incoming.events.proto.ProtoCashTransferEvent
import com.lykke.me.test.client.incoming.events.proto.ProtocolEvents

class ProtoCashTransferEventDeserializer : MeProtoEventDeserializer<ProtocolEvents.CashTransferEvent> {
    override fun deserialize(byteArray: ByteArray): MeProtoEvent<ProtocolEvents.CashTransferEvent> {
        return ProtoCashTransferEvent(ProtocolEvents.CashTransferEvent.parseFrom(byteArray))
    }
}