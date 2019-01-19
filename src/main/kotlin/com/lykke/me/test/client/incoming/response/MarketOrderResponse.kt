package com.lykke.me.test.client.incoming.response

import java.math.BigDecimal

class MarketOrderResponse(val requestId: String,
                          val messageId: String,
                          val status: MessageStatus,
                          val statusReason: String?,
                          val price: BigDecimal?): Response