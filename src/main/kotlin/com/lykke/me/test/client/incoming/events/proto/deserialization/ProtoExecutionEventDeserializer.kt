package com.lykke.me.test.client.incoming.events.proto.deserialization

import com.lykke.me.test.client.incoming.events.proto.MeProtoEvent
import com.lykke.me.test.client.incoming.events.proto.ProtoExecutionEvent
import com.lykke.me.test.client.incoming.events.proto.ProtocolEvents

class ProtoExecutionEventDeserializer : MeProtoEventDeserializer<ProtocolEvents.ExecutionEvent> {
    override fun deserialize(byteArray: ByteArray): MeProtoEvent<ProtocolEvents.ExecutionEvent> {
        return ProtoExecutionEvent(ProtocolEvents.ExecutionEvent.parseFrom(byteArray))
    }


}