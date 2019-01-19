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
import java.util.UUID

interface MessageBuilder {
    fun buildCashInOutMessage(clientId: String,
                              assetId: String,
                              volume: BigDecimal,
                              fees: Collection<SimpleFeeInstruction> = emptyList(),
                              requestId: String = UUID.randomUUID().toString(),
                              messageId: String? = null,
                              date: Date = Date()): CashInOutMessage

    fun buildCashTransferMessage(fromClientId: String,
                                 toClientId: String,
                                 assetId: String,
                                 volume: BigDecimal,
                                 fee: SimpleFeeInstruction? = null,
                                 overdraftLimit: BigDecimal? = null,
                                 fees: List<SimpleFeeInstruction> = emptyList(),
                                 requestId: String = UUID.randomUUID().toString(),
                                 messageId: String? = null,
                                 date: Date = Date()): CashTransferMessage

    fun buildLimitOrderMessage(clientId: String,
                               assetPairId: String,
                               volume: BigDecimal,
                               price: BigDecimal?,
                               fee: FeeInstruction? = null,
                               fees: List<FeeInstruction> = emptyList(),
                               externalId: String = UUID.randomUUID().toString(),
                               type: LimitOrderType? = null,
                               lowerLimitPrice: BigDecimal? = null,
                               lowerPrice: BigDecimal? = null,
                               upperLimitPrice: BigDecimal? = null,
                               upperPrice: BigDecimal? = null,
                               messageId: String? = null,
                               cancelAllPreviousLimitOrders: Boolean? = null,
                               timeInForce: TimeInForce? = null,
                               expiryTime: Date? = null,
                               date: Date = Date()): LimitOrderMessage

    fun buildLimitOrderCancelMessage(orderIds: Collection<String>,
                                     requestId: String = UUID.randomUUID().toString(),
                                     messageId: String? = null): LimitOrderCancelMessage

    fun buildLimitOrderMassCancelMessage(clientId: String,
                                         assetPairId: String? = null,
                                         isBuy: Boolean? = null,
                                         requestId: String = UUID.randomUUID().toString(),
                                         messageId: String? = null): LimitOrderMassCancelMessage

    fun buildMarketOrderMessage(clientId: String,
                                assetPairId: String,
                                volume: BigDecimal,
                                straight: Boolean = true,
                                reservedLimitVolume: BigDecimal? = null,
                                fee: SimpleFeeInstruction? = null,
                                fees: List<SimpleFeeInstruction> = emptyList(),
                                externalId: String = UUID.randomUUID().toString(),
                                messageId: String? = null,
                                date: Date = Date()): MarketOrderMessage

    fun buildMultiLimitOrderCancelMessage(clientId: String,
                                          assetPairId: String,
                                          isBuy: Boolean,
                                          requestId: String = UUID.randomUUID().toString(),
                                          messageId: String? = null,
                                          date: Date = Date()): MultiLimitOrderCancelMessage

    fun buildMultiLimitOrderMessage(clientId: String,
                                    assetPairId: String,
                                    orders: List<MultiLimitOrderMessage.LimitOrder>,
                                    cancelAllPreviousLimitOrders: Boolean? = true,
                                    cancelMode: OrderCancelMode? = null,
                                    requestId: String = UUID.randomUUID().toString(),
                                    messageId: String? = null,
                                    date: Date = Date()): MultiLimitOrderMessage

    fun buildReservedCashInOutMessage(clientId: String,
                                      assetId: String,
                                      reservedVolume: BigDecimal,
                                      requestId: String = UUID.randomUUID().toString(),
                                      messageId: String? = null,
                                      date: Date = Date()): ReservedCashInOutMessage

    fun buildMultiLimitOneOrder(price: BigDecimal?,
                                volume: BigDecimal,
                                fee: FeeInstruction? = null,
                                fees: List<FeeInstruction> = emptyList(),
                                externalId: String = UUID.randomUUID().toString(),
                                oldUid: String? = null,
                                timeInForce: TimeInForce? = null,
                                expiryTime: Date? = null,
                                type: LimitOrderType? = null,
                                lowerLimitPrice: BigDecimal? = null,
                                lowerPrice: BigDecimal? = null,
                                upperLimitPrice: BigDecimal? = null,
                                upperPrice: BigDecimal? = null): MultiLimitOrderMessage.LimitOrder
}