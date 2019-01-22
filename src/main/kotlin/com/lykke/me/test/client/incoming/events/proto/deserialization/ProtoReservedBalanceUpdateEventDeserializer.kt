package com.lykke.me.test.client.incoming.events.proto.deserialization

import com.lykke.me.test.client.incoming.events.proto.MeProtoEvent
import com.lykke.me.test.client.incoming.events.proto.ProtoReservedBalanceUpdateEvent
import com.lykke.me.test.client.incoming.events.proto.ProtocolEvents

class ProtoReservedBalanceUpdateEventDeserializer : MeProtoEventDeserializer<ProtocolEvents.ReservedBalanceUpdateEvent> {
    override fun deserialize(byteArray: ByteArray): MeProtoEvent<ProtocolEvents.ReservedBalanceUpdateEvent> {
        val message = ProtocolEvents.ReservedBalanceUpdateEvent.parseFrom(byteArray)
        return ProtoReservedBalanceUpdateEvent(message, message.header.messageId)
    }

}