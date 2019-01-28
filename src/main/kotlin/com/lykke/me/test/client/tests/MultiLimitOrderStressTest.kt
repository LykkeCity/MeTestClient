package com.lykke.me.test.client.tests

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.entity.Asset
import com.lykke.me.test.client.entity.AssetPair
import com.lykke.me.test.client.outgoing.messages.MultiLimitOrderMessage
import com.lykke.me.test.client.outgoing.messages.common.OrderCancelMode
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import org.apache.log4j.Logger
import java.math.BigDecimal
import java.math.RoundingMode

class MultiLimitOrderStressTest(private val meClient: MeClient,
                                private val messageBuilder: MessageBuilder) {

    companion object {
        private val LOGGER = Logger.getLogger(MultiLimitOrderStressTest::class.java.name)
    }

    private val trustedClientId = "Client4"
    private val clientId = "Client1"
    private val initialOrderBookClientId = "Client5"
    private val oppositeClientIds = setOf("Client2", "Client3", initialOrderBookClientId)
    private val assetPair = AssetPair("TestMeAsset1_TestMeAsset2",
            3,
            Asset("TestMeAsset1", 2),
            Asset("TestMeAsset2", 2))

    private val TEST_COUNT = 1
    private val SIDE_ORDERS_COUNT = 200
    private val DELAY = 3000L
    private val startAskPrice = BigDecimal("11")
    private val startBidPrice = BigDecimal("9")
    private val priceStep = BigDecimal("0.01")
    private val volume = BigDecimal("2")
    private val ordersCountToMatch = 1
    private val initialOrderBookSideSize = 10

    private val assetIds = listOf(assetPair.baseAsset.id, assetPair.quotingAsset.id)
    private val allClientIds = {
        val result = oppositeClientIds.toMutableList()
        result.add(trustedClientId)
        result.add(clientId)
        result.toList()
    }()

    private val assetPairIds = listOf(assetPair.id)


    @MeTest
    fun testTrustedClientMultiLimitOrderWithoutTrades() {
        testMultiLimitOrderWithoutTrades(trustedClientId)
    }

    @MeTest
    fun testTrustedClientMultiLimitOrderWithTrades() {
        testMultiLimitOrderWithTrades(trustedClientId)
    }

    @MeTest
    fun testClientMultiLimitOrderWithoutTrades() {
        testMultiLimitOrderWithoutTrades(clientId)
    }

    @MeTest
    fun testClientMultiLimitOrderWithTrades() {
        testMultiLimitOrderWithTrades(clientId)
    }

    private fun testMultiLimitOrderWithoutTrades(clientId: String) {
        init()
        (1..TEST_COUNT).forEach {
            meClient.sendMessage(buildMultiLimitOrderMessage(clientId, SIDE_ORDERS_COUNT))
            LOGGER.debug("$it/$TEST_COUNT")
            Thread.sleep(DELAY)
        }
    }

    private fun testMultiLimitOrderWithTrades(clientId: String) {
        init()
        (1..TEST_COUNT).forEach {
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
                    SIDE_ORDERS_COUNT,
                    startAskPrice,
                    startBidPrice))
            LOGGER.debug("$it/$TEST_COUNT")
            Thread.sleep(DELAY)
        }
    }

    private fun init() {
        allClientIds.forEach { clientId ->
            assetPairIds.forEach { assetPairId ->
                meClient.sendMessage(messageBuilder.buildLimitOrderMassCancelMessage(clientId, assetPairId))
            }
            assetIds.forEach { assetId ->
                meClient.sendMessage(messageBuilder.buildCashInOutMessage(clientId, assetId, BigDecimal(1000000)))
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
        meClient.sendMessage(messageBuilder.buildMultiLimitOrderMessage(initialOrderBookClientId,
                assetPair.id,
                orders))
        Thread.sleep(1000)
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