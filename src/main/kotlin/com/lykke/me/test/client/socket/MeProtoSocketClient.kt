package com.lykke.me.test.client.socket

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.serialization.proto.factories.MessageProtoSerializerFactory

class MeProtoSocketClient : MeClient {

    override fun sendMessage(message: Message) {
        val protoMessage = MessageProtoSerializerFactory
                .getFactory(message.getType())
                .createSerializer()
                .serialize(message)

        println(protoMessage.messageType)
        println(protoMessage.generatedMessage)

        // todo
    }
}