package com.lykke.me.test.client.incoming.events.proto.deserialization

import com.lykke.me.test.client.incoming.events.proto.MeProtoEvent
import com.lykke.me.test.client.incoming.events.proto.ProtoCashTransferEvent
import com.lykke.me.test.client.incoming.events.proto.ProtocolEvents

class ProtoCashTransferEventDeserializer : MeProtoEventDeserializer<ProtocolEvents.CashTransferEvent> {
    override fun deserialize(byteArray: ByteArray): MeProtoEvent<ProtocolEvents.CashTransferEvent> {
        val message = ProtocolEvents.CashTransferEvent.parseFrom(byteArray)
        return ProtoCashTransferEvent(message, message.header.messageId)
    }
}