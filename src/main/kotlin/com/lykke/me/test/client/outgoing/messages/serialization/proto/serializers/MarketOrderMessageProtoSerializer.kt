package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.MarketOrderMessage
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class MarketOrderMessageProtoSerializer : MessageProtoSerializer {

    override fun serialize(message: Message): ProtoMessageWrapper {
        message as MarketOrderMessage
        val builder = ProtocolMessages.MarketOrder.newBuilder()
                .setUid(message.externalId)
                .setTimestamp(message.date.time)
                .setClientId(message.clientId)
                .setAssetPairId(message.assetPairId)
                .setVolume(message.volume.toDouble())
                .setStraight(message.straight)
        message.messageId?.let {
            builder.messageId = it
        }
        message.reservedLimitVolume?.let {
            builder.reservedLimitVolume = it.toDouble()
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