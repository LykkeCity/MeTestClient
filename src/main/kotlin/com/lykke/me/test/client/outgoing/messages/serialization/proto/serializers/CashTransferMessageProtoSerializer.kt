package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.CashTransferMessage
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class CashTransferMessageProtoSerializer : MessageProtoSerializer {

    override fun serialize(message: Message): ProtoMessageWrapper {
        message as CashTransferMessage
        val builder = ProtocolMessages.CashTransferOperation.newBuilder()
                .setId(message.requestId)
                .setTimestamp(message.date.time)
                .setFromClientId(message.fromClientId)
                .setToClientId(message.toClientId)
                .setAssetId(message.assetId)
                .setVolume(message.volume.toDouble())
        message.overdraftLimit?.let {
            builder.setOverdraftLimit(it.toDouble())
        }
        message.messageId?.let {
            builder.messageId = it
        }
        message.fee?.let {
            builder.setFee(ProtoUtils.createFeeBuilder(it))
        }
        message.fees.forEach {
            builder.addFees(ProtoUtils.createFeeBuilder(it))
        }
        return ProtoMessageWrapper(builder.build(), message.getType())
    }

}