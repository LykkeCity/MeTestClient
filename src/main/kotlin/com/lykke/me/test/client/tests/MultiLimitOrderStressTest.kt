package com.lykke.me.test.client.tests

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.entity.Asset
import com.lykke.me.test.client.entity.AssetPair
import com.lykke.me.test.client.outgoing.messages.MultiLimitOrderMessage
import com.lykke.me.test.client.outgoing.messages.common.OrderCancelMode
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import org.apache.log4j.Logger
import java.math.BigDecimal
import java.math.RoundingMode

class MultiLimitOrderStressTest(private val meClient: MeClient,
                                private val meClientForSyncInteraction: MeClient,
                                private val messageBuilder: MessageBuilder,
                                config: Config) {

    companion object {
        private val LOGGER = Logger.getLogger(MultiLimitOrderStressTest::class.java.name)
    }

    private val multiOrderStressTestConfig = config.matchingEngineTestClient.multiOrderStressTest
    private val testPrerequisitesConfig = config.matchingEngineTestClient.testPrerequisitesConfig
    private val configClientIds = testPrerequisitesConfig.clientsConfig.clients.toList()

    private val trustedClientId = testPrerequisitesConfig.clientsConfig.trustedClients.first()
    private val clientId = configClientIds[0]
    private val initialOrderBookClientId = configClientIds[1]
    private val oppositeClientIds = setOf(configClientIds[2], configClientIds[3], initialOrderBookClientId)
    private val allClientIds = {
        val result = oppositeClientIds.toMutableList()
        result.add(trustedClientId)
        result.add(clientId)
        result.toList()
    }()

    private val assetPairConfig = testPrerequisitesConfig.assetsConfig.first()
    private val assetPair = AssetPair(assetPairConfig.assetPairId,
            assetPairConfig.assetPairAccuracy,
            Asset(assetPairConfig.baseAssetId, assetPairConfig.baseAssetAccuracy),
            Asset(assetPairConfig.quotingAssetId, assetPairConfig.quotingAssetAccuracy))
    private val assetIds = listOf(assetPair.baseAsset.id, assetPair.quotingAsset.id)
    private val assetPairIds = listOf(assetPair.id)

    private val testsCount = multiOrderStressTestConfig.testsCount
    private val multiOrderSideSize = multiOrderStressTestConfig.multiOrderSideSize
    private val startAskPrice = BigDecimal(multiOrderStressTestConfig.startAskPrice.toString())
    private val startBidPrice = BigDecimal(multiOrderStressTestConfig.startBidPrice.toString())
    private val priceStep = BigDecimal(multiOrderStressTestConfig.priceStep.toString())
    private val volume = BigDecimal(multiOrderStressTestConfig.volume.toString())
    private val ordersCountToMatch = multiOrderStressTestConfig.ordersCountToMatch
    private val initialOrderBookSideSize = testPrerequisitesConfig.maxOrdersInOrderBook / 2

    @MeTest
    fun trustedClientMultiLimitOrderWithoutTrades() {
        testMultiLimitOrderWithoutTrades(trustedClientId)
    }

    @MeTest
    fun trustedClientMultiLimitOrderWithTrades() {
        testMultiLimitOrderWithTrades(trustedClientId)
    }

    @MeTest
    fun clientMultiLimitOrderWithoutTrades() {
        testMultiLimitOrderWithoutTrades(clientId)
    }

    @MeTest
    fun clientMultiLimitOrderWithTrades() {
        testMultiLimitOrderWithTrades(clientId)
    }

    private fun testMultiLimitOrderWithoutTrades(clientId: String) {
        init()
        (1..testsCount).forEach {
            meClient.sendMessage(buildMultiLimitOrderMessage(clientId, multiOrderSideSize))
            LOGGER.debug("$it/$testsCount")
        }
    }

    private fun testMultiLimitOrderWithTrades(clientId: String) {
        init()
        (1..testsCount).forEach {
            val isBuySideTrades = it % 2 == 0
            setOppositeOrderBookForTrades(!isBuySideTrades, ordersCountToMatch)

            val startAskPrice: BigDecimal
            val startBidPrice: BigDecimal

            if (isBuySideTrades) {
                startBidPrice = this.startBidPrice
                startAskPrice = (this.startAskPrice + this.priceStep + BigDecimal(2) * BigDecimal(ordersCountToMatch) * this.priceStep)
            } else {
                startAskPrice = this.startAskPrice
                startBidPrice = (this.startBidPrice - this.priceStep - BigDecimal(2) * BigDecimal(ordersCountToMatch) * this.priceStep)
            }

            meClient.sendMessage(buildMultiLimitOrderMessage(clientId,
                    multiOrderSideSize,
                    startAskPrice,
                    startBidPrice))
            LOGGER.debug("$it/$testsCount")
        }
    }

    private fun init() {
        allClientIds.forEach { clientId ->
            assetPairIds.forEach { assetPairId ->
                meClientForSyncInteraction.sendMessage(messageBuilder.buildLimitOrderMassCancelMessage(clientId, assetPairId))
            }
            assetIds.forEach { assetId ->
                meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(clientId, assetId, BigDecimal(1000000)))
            }
        }
        val orders = ArrayList<MultiLimitOrderMessage.LimitOrder>(2 * initialOrderBookSideSize)
        orders.addAll(buildOrders(BigDecimal("99999"),
                -volume,
                BigDecimal.ZERO,
                initialOrderBookSideSize))
        orders.addAll(buildOrders(BigDecimal("0.0000000001").setScale(assetPair.accuracy, RoundingMode.UP),
                volume,
                BigDecimal.ZERO,
                initialOrderBookSideSize))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildMultiLimitOrderMessage(initialOrderBookClientId,
                assetPair.id,
                orders))
    }

    private fun buildMultiLimitOrderMessage(clientId: String,
                                            sideCount: Int,
                                            startAskPrice: BigDecimal = this.startAskPrice,
                                            startBidPrice: BigDecimal = this.startBidPrice): MultiLimitOrderMessage {
        val orders = ArrayList<MultiLimitOrderMessage.LimitOrder>(2 * sideCount)
        orders.addAll(buildOrders(startAskPrice, -volume, priceStep, sideCount))
        orders.addAll(buildOrders(startBidPrice, volume, -priceStep, sideCount))
        return messageBuilder.buildMultiLimitOrderMessage(clientId,
                assetPair.id,
                orders,
                true,
                OrderCancelMode.BOTH_SIDES)
    }

    private fun buildOrders(startPrice: BigDecimal,
                            volume: BigDecimal,
                            delta: BigDecimal,
                            count: Int): List<MultiLimitOrderMessage.LimitOrder> {
        return (1..count).map {
            val price = (startPrice + delta * BigDecimal(it - 1)).setScale(assetPair.accuracy, RoundingMode.HALF_UP)
            messageBuilder.buildMultiLimitOneOrder(
                    price,
                    volume
            )
        }
    }

    private fun setOppositeOrderBookForTrades(isBuy: Boolean,
                                              count: Int) {

        val startPrice: BigDecimal
        val volume: BigDecimal
        val priceStep: BigDecimal
        val ordersByClientId = HashMap<String, MutableList<MultiLimitOrderMessage.LimitOrder>>()
        val clientIds = oppositeClientIds.toList()
        var clientIdIndex = 0
        val coef = BigDecimal("0.8")

        if (isBuy) {
            startPrice = startAskPrice + (this.priceStep * (2 * count).toBigDecimal() - this.priceStep.setScale(this.priceStep.scale() + 1).divide(BigDecimal("2.0")))
            priceStep = -this.priceStep
            volume = (this.volume * coef).setScale(assetPair.baseAsset.accuracy, RoundingMode.HALF_UP)
        } else {
            startPrice = startBidPrice - (this.priceStep * (2 * count).toBigDecimal() - this.priceStep.setScale(this.priceStep.scale() + 1).divide(BigDecimal("2.0")))
            priceStep = this.priceStep
            volume = -(this.volume * coef).setScale(assetPair.baseAsset.accuracy, RoundingMode.HALF_UP)
        }

        (1..count).forEach {
            val order = messageBuilder.buildMultiLimitOneOrder(
                    (startPrice + BigDecimal(it) * priceStep).setScale(assetPair.accuracy, RoundingMode.HALF_UP),
                    volume
            )

            val clientId = clientIds[clientIdIndex]
            ordersByClientId.getOrPut(clientId) { mutableListOf() }
                    .add(order)

            clientIdIndex++
            if (clientIdIndex == clientIds.size) {
                clientIdIndex = 0
            }
        }

        ordersByClientId.forEach { clientId, orders ->
            meClient.sendMessage(messageBuilder.buildMultiLimitOrderMessage(clientId,
                    assetPair.id,
                    orders,
                    true,
                    OrderCancelMode.BOTH_SIDES))
        }
    }

}