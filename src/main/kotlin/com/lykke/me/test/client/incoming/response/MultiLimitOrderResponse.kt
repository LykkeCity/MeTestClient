package com.lykke.me.test.client.incoming.response

import java.math.BigDecimal

class MultiLimitOrderResponse(val requestId: String,
                              override val messageId: String,
                              val status: MessageStatus,
                              val statusReason: String?,
                              val assetPairId: String,
                              val orderStatuses: List<OrderStatus>) : Response {

    class OrderStatus(val externalId: String,
                      val matchingEngineId: String?,
                      val status: MessageStatus,
                      val statusReason: String?,
                      val volume: BigDecimal,
                      val price: BigDecimal)
}