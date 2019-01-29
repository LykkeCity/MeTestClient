package com.lykke.me.test.client.config

import java.math.BigDecimal

class LimitOrderTestConfig(val startAskPrice: BigDecimal,
                           val startBidPrice: BigDecimal,
                           val priceStep: BigDecimal,
                           val volume: BigDecimal)