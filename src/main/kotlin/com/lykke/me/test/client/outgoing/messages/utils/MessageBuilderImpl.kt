package com.lykke.me.test.client.outgoing.messages.utils

import com.lykke.me.test.client.outgoing.messages.CashInOutMessage
import com.lykke.me.test.client.outgoing.messages.CashTransferMessage
import com.lykke.me.test.client.outgoing.messages.LimitOrderCancelMessage
import com.lykke.me.test.client.outgoing.messages.LimitOrderMassCancelMessage
import com.lykke.me.test.client.outgoing.messages.LimitOrderMessage
import com.lykke.me.test.client.outgoing.messages.MarketOrderMessage
import com.lykke.me.test.client.outgoing.messages.MultiLimitOrderCancelMessage
import com.lykke.me.test.client.outgoing.messages.MultiLimitOrderMessage
import com.lykke.me.test.client.outgoing.messages.ReservedCashInOutMessage
import com.lykke.me.test.client.outgoing.messages.common.FeeInstruction
import com.lykke.me.test.client.outgoing.messages.common.LimitOrderType
import com.lykke.me.test.client.outgoing.messages.common.OrderCancelMode
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import com.lykke.me.test.client.outgoing.messages.common.TimeInForce
import java.math.BigDecimal
import java.util.Date

class MessageBuilderImpl : MessageBuilder {

    override fun buildCashInOutMessage(clientId: String,
                                       assetId: String,
                                       volume: BigDecimal,
                                       fees: Collection<SimpleFeeInstruction>,
                                       requestId: String,
                                       messageId: String?,
                                       date: Date): CashInOutMessage {
        return CashInOutMessage(clientId,
                assetId,
                volume,
                fees,
                requestId,
                messageId,
                date)
    }

    override fun buildCashTransferMessage(fromClientId: String,
                                          toClientId: String,
                                          assetId: String,
                                          volume: BigDecimal,
                                          fee: SimpleFeeInstruction?,
                                          overdraftLimit: BigDecimal?,
                                          fees: List<SimpleFeeInstruction>,
                                          requestId: String,
                                          messageId: String?,
                                          date: Date): CashTransferMessage {
        return CashTransferMessage(fromClientId,
                toClientId,
                assetId,
                volume,
                fee,
                overdraftLimit,
                fees,
                requestId,
                messageId,
                date)
    }

    override fun buildLimitOrderMessage(clientId: String,
                                        assetPairId: String,
                                        volume: BigDecimal,
                                        price: BigDecimal?,
                                        fee: FeeInstruction?,
                                        fees: List<FeeInstruction>,
                                        externalId: String,
                                        type: LimitOrderType?,
                                        lowerLimitPrice: BigDecimal?,
                                        lowerPrice: BigDecimal?,
                                        upperLimitPrice: BigDecimal?,
                                        upperPrice: BigDecimal?,
                                        messageId: String?,
                                        cancelAllPreviousLimitOrders: Boolean?,
                                        timeInForce: TimeInForce?,
                                        expiryTime: Date?,
                                        date: Date): LimitOrderMessage {
        return LimitOrderMessage(clientId,
                assetPairId,
                volume,
                price,
                fee,
                fees,
                externalId,
                type,
                lowerLimitPrice,
                lowerPrice,
                upperLimitPrice,
                upperPrice,
                messageId,
                cancelAllPreviousLimitOrders,
                timeInForce,
                expiryTime,
                date)
    }

    override fun buildLimitOrderCancelMessage(orderIds: Collection<String>,
                                              requestId: String,
                                              messageId: String?): LimitOrderCancelMessage {
        return LimitOrderCancelMessage(orderIds,
                requestId,
                messageId)
    }

    override fun buildLimitOrderMassCancelMessage(clientId: String?,
                                                  assetPairId: String?,
                                                  isBuy: Boolean?,
                                                  requestId: String,
                                                  messageId: String?): LimitOrderMassCancelMessage {
        return LimitOrderMassCancelMessage(clientId,
                assetPairId,
                isBuy,
                requestId,
                messageId)
    }

    override fun buildMarketOrderMessage(clientId: String,
                                         assetPairId: String,
                                         volume: BigDecimal,
                                         straight: Boolean,
                                         reservedLimitVolume: BigDecimal?,
                                         fee: SimpleFeeInstruction?,
                                         fees: List<SimpleFeeInstruction>,
                                         externalId: String,
                                         messageId: String?,
                                         date: Date): MarketOrderMessage {
        return MarketOrderMessage(clientId,
                assetPairId,
                volume,
                straight,
                reservedLimitVolume,
                fee,
                fees,
                externalId,
                messageId,
                date)
    }

    override fun buildMultiLimitOrderCancelMessage(clientId: String,
                                                   assetPairId: String,
                                                   isBuy: Boolean,
                                                   requestId: String,
                                                   messageId: String?,
                                                   date: Date): MultiLimitOrderCancelMessage {
        return MultiLimitOrderCancelMessage(clientId,
                assetPairId,
                isBuy,
                requestId,
                messageId,
                date)
    }

    override fun buildMultiLimitOrderMessage(clientId: String,
                                             assetPairId: String,
                                             orders: List<MultiLimitOrderMessage.LimitOrder>,
                                             cancelAllPreviousLimitOrders: Boolean?,
                                             cancelMode: OrderCancelMode?,
                                             requestId: String,
                                             messageId: String?,
                                             date: Date): MultiLimitOrderMessage {
        return MultiLimitOrderMessage(clientId,
                assetPairId,
                orders,
                cancelAllPreviousLimitOrders,
                cancelMode,
                requestId,
                messageId,
                date)
    }

    override fun buildReservedCashInOutMessage(clientId: String,
                                               assetId: String,
                                               reservedVolume: BigDecimal,
                                               requestId: String,
                                               messageId: String?,
                                               date: Date): ReservedCashInOutMessage {
        return ReservedCashInOutMessage(clientId,
                assetId,
                reservedVolume,
                requestId,
                messageId,
                date)
    }

    override fun buildMultiLimitOneOrder(price: BigDecimal?,
                                         volume: BigDecimal,
                                         fee: FeeInstruction?,
                                         fees: List<FeeInstruction>,
                                         externalId: String,
                                         oldUid: String?,
                                         timeInForce: TimeInForce?,
                                         expiryTime: Date?,
                                         type: LimitOrderType?,
                                         lowerLimitPrice: BigDecimal?,
                                         lowerPrice: BigDecimal?,
                                         upperLimitPrice: BigDecimal?,
                                         upperPrice: BigDecimal?): MultiLimitOrderMessage.LimitOrder {
        return MultiLimitOrderMessage.LimitOrder(price,
                volume,
                fee,
                fees,
                externalId,
                oldUid,
                timeInForce,
                expiryTime,
                type,
                lowerLimitPrice,
                lowerPrice,
                upperLimitPrice,
                upperPrice)
    }
}