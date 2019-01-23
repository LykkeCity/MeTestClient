package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.LimitOrderMessage
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class LimitOrderMessageProtoSerializer : MessageProtoSerializer {
    override fun serialize(message: Message): ProtoMessageWrapper {
        message as LimitOrderMessage
        val builder = ProtocolMessages.LimitOrder.newBuilder()
                .setUid(message.externalId)
                .setTimestamp(message.date.time)
                .setClientId(message.clientId)
                .setAssetPairId(message.assetPairId)
                .setVolume(message.volume.toDouble())
        message.messageId?.let {
            builder.messageId = it
        }
        message.price?.let {
            builder.price = it.toDouble()
        }
        message.type?.let {
            builder.type = it.externalId
        }
        message.lowerLimitPrice?.let {
            builder.lowerLimitPrice = it.toDouble()
        }
        message.lowerPrice?.let {
            builder.lowerPrice = it.toDouble()
        }
        message.upperLimitPrice?.let {
            builder.upperLimitPrice = it.toDouble()
        }
        message.upperPrice?.let {
            builder.upperPrice = it.toDouble()
        }
        message.fee?.let {
            builder.setFee(ProtoUtils.createFeeBuilder(it))
        }
        message.fees.forEach {
            builder.addFees(ProtoUtils.createFeeBuilder(it))
        }
        message.timeInForce?.let {
            builder.timeInForce = it.externalId
        }
        message.expiryTime?.let {
            builder.expiryTime = it.time
        }
        message.cancelAllPreviousLimitOrders?.let {
            builder.cancelAllPreviousLimitOrders = it
        }
        return ProtoMessageWrapper(builder.build(), message)
    }
}