package com.lykke.me.test.client.outgoing.messages.serialization.proto.factories

import com.lykke.me.test.client.outgoing.messages.common.MessageType
import com.lykke.me.test.client.outgoing.messages.serialization.MessageSerializerFactory
import com.lykke.me.test.client.outgoing.messages.serialization.proto.serializers.ProtoMessageWrapper
import java.lang.IllegalArgumentException

interface MessageProtoSerializerFactory : MessageSerializerFactory<ProtoMessageWrapper> {

    companion object {

        private val serializerFactoryByMessageType = mapOf(
                Pair(MessageType.CASH_IN_OUT_OPERATION, CashInOutMessageProtoSerializerFactory()),
                Pair(MessageType.CASH_TRANSFER_OPERATION, CashTransferMessageProtoSerializerFactory()),
                Pair(MessageType.LIMIT_ORDER_CANCEL, LimitOrderCancelMessageProtoSerializerFactory()),
                Pair(MessageType.LIMIT_ORDER_MASS_CANCEL, LimitOrderMassCancelMessageProtoSerializerFactory()),
                Pair(MessageType.LIMIT_ORDER, LimitOrderMessageProtoSerializerFactory()),
                Pair(MessageType.MARKET_ORDER, MarketOrderMessageProtoSerializerFactory()),
                Pair(MessageType.MULTI_LIMIT_ORDER_CANCEL, MultiLimitOrderCancelMessageSerializerFactory()),
                Pair(MessageType.MULTI_LIMIT_ORDER, MultiLimitOrderMessageProtoSerializerFactory()),
                Pair(MessageType.RESERVED_CASH_IN_OUT_OPERATION, ReservedCashInOutMessageProtoSerializerFactory())
        )

        fun getFactory(messageType: MessageType): MessageProtoSerializerFactory {
            return serializerFactoryByMessageType[messageType]
                    ?: throw IllegalArgumentException("Unsupported message type: $messageType")
        }
    }
}