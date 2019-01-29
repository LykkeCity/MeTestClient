package com.lykke.me.test.client.config

data class MultiOrderStressTestConfig(val testsCount: Int,
                                      val multiOrderSideSize: Int,
                                      val startAskPrice: Double,
                                      val startBidPrice: Double,
                                      val priceStep: Double,
                                      val volume: Double,
                                      val ordersCountToMatch: Int)