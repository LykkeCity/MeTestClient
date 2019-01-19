package com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers

import com.lykke.me.test.client.outgoing.messages.common.FeeInstruction
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import com.lykke.me.test.client.outgoing.messages.proto.ProtocolMessages

class ProtoUtils {
    companion object {
        fun createFeeBuilder(fee: SimpleFeeInstruction): ProtocolMessages.Fee.Builder {
            val builder = ProtocolMessages.Fee.newBuilder().setType(fee.type.externalId)
            fee.size?.let {
                builder.size = it.toDouble()
            }
            fee.sourceClientId?.let {
                builder.setSourceClientId(it)
            }
            fee.targetClientId?.let {
                builder.setTargetClientId(it)
            }
            fee.sizeType?.let {
                builder.setSizeType(it.externalId)
            }
            return builder
        }

        fun createFeeBuilder(fee: FeeInstruction): ProtocolMessages.LimitOrderFee.Builder {
            val builder = ProtocolMessages.LimitOrderFee.newBuilder().setType(fee.type.externalId)
            fee.takerSize?.let {
                builder.takerSize = it.toDouble()
            }
            fee.takerSizeType?.let {
                builder.takerSizeType = it.externalId
            }
            fee.makerSize?.let {
                builder.makerSize = it.toDouble()
            }
            fee.makerSizeType?.let {
                builder.makerSizeType = it.externalId
            }
            fee.sourceClientId?.let {
                builder.setSourceClientId(it)
            }
            fee.targetClientId?.let {
                builder.setTargetClientId(it)
            }
            builder.addAllAssetId(fee.assetIds)
            return builder
        }
    }
}