package com.lykke.me.test.client.incoming.events.proto.deserialization

import com.google.protobuf.GeneratedMessageV3
import com.lykke.me.test.client.incoming.events.proto.MeProtoEvent
import java.lang.IllegalArgumentException

interface MeProtoEventDeserializer<T : GeneratedMessageV3> {

    companion object {
        fun createDeserializer(routingKey: String): MeProtoEventDeserializer<*> {
            return when (routingKey) {
                "1" -> ProtoCashInEventDeserializer()
                "2" -> ProtoCashOutEventDeserializer()
                "3" -> ProtoCashTransferEventDeserializer()
                "4" -> ProtoExecutionEventDeserializer()
                "5" -> ProtoReservedBalanceUpdateEventDeserializer()
                else -> throw IllegalArgumentException("There is no deserializer for routingKey=$routingKey")
            }
        }
    }

    fun deserialize(byteArray: ByteArray): MeProtoEvent<T>
}