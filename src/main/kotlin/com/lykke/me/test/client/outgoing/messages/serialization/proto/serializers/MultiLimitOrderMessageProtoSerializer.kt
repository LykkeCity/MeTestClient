package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.MultiLimitOrderMessage
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class MultiLimitOrderMessageProtoSerializer : MessageProtoSerializer {
    override fun serialize(message: Message): ProtoMessageWrapper {
        message as MultiLimitOrderMessage
        val builder = ProtocolMessages.MultiLimitOrder.newBuilder()
                .setUid(message.requestId)
                .setTimestamp(message.date.time)
                .setClientId(message.clientId)
                .setAssetPairId(message.assetPairId)
        message.cancelMode?.let {
            builder.cancelMode = it.externalId
        }
        message.cancelAllPreviousLimitOrders?.let {
            builder.cancelAllPreviousLimitOrders = it
        }
        message.messageId?.let {
            builder.messageId = it
        }
        message.orders.forEach { limitOrder ->
            val orderBuilder = ProtocolMessages.MultiLimitOrder.Order.newBuilder()
                    .setUid(limitOrder.externalId)
                    .setVolume(limitOrder.volume.toDouble())
            limitOrder.price?.let { price -> orderBuilder.price = price.toDouble() }
            limitOrder.type?.let { type -> orderBuilder.type = type.externalId }
            limitOrder.lowerLimitPrice?.let {
                lowerLimitPrice -> orderBuilder.lowerLimitPrice = lowerLimitPrice.toDouble()
            }
            limitOrder.lowerPrice?.let {
                lowerPrice -> orderBuilder.lowerPrice = lowerPrice.toDouble()
            }
            limitOrder.upperLimitPrice?.let {
                upperLimitPrice -> orderBuilder.upperLimitPrice = upperLimitPrice.toDouble()
            }
            limitOrder.upperPrice?.let {
                upperPrice -> orderBuilder.upperPrice = upperPrice.toDouble()
            }
            limitOrder.fee?.let {
                orderBuilder.setFee(ProtoUtils.createFeeBuilder(it))
            }
            limitOrder.fees.forEach {
                orderBuilder.addFees(ProtoUtils.createFeeBuilder(it))
            }
            limitOrder.oldUid?.let { orderBuilder.oldUid = it }
            limitOrder.timeInForce?.let { orderBuilder.timeInForce = it.externalId }
            limitOrder.expiryTime?.let { orderBuilder.expiryTime = it.time }
            builder.addOrders(orderBuilder.build())
        }
        return ProtoMessageWrapper(builder.build(), message.getType())
    }
}