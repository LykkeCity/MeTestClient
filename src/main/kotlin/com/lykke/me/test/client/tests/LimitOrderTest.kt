package com.lykke.me.test.client.tests

import com.google.common.util.concurrent.AtomicDouble
import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.config.TestPrerequisitesConfig
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import com.lykke.me.test.client.utils.generateMessages
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import javax.annotation.PostConstruct

@MeTest(3)
class LimitOrderTest {
    @Autowired
    private lateinit var meClient: MeClient

    @Autowired
    private lateinit var messageBuilder: MessageBuilder

    @Autowired
    private lateinit var config: Config

    private lateinit var CLIENT1: String
    private lateinit var CLIENT2: String
    private lateinit var ASSET_PAIR: TestPrerequisitesConfig.AssetPairConfig
    private var MAX_ORDERS_IN_ORDER_BOOK: Int? = null

    @PostConstruct
    fun init() {
        CLIENT1 = config.matchingEngineTestClient.testPrerequisitesConfig.clientsConfig.clients.toList()[0]
        CLIENT2 = config.matchingEngineTestClient.testPrerequisitesConfig.clientsConfig.clients.toList()[1]
        ASSET_PAIR = config.matchingEngineTestClient.testPrerequisitesConfig.assetsConfig.toList()[1]
        MAX_ORDERS_IN_ORDER_BOOK = config.matchingEngineTestClient.testPrerequisitesConfig.maxOrdersInOrderBook
    }

    fun testOrderNotMatch() {
        cancelAllClientOrders(assetPairId =  ASSET_PAIR.assetPairId)
        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.quotingAssetId, BigDecimal.valueOf(-300_000)))
        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.quotingAssetId, BigDecimal.valueOf(300_000)))

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.assetPairId,
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)
    }

    fun testOrderFullyMatchedOneLevel() {
        cancelAllClientOrders(assetPairId =  ASSET_PAIR.assetPairId)
        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAssetId, BigDecimal.valueOf(-300_000)))
        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAssetId, BigDecimal.valueOf(-100)))
        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAssetId, BigDecimal.valueOf(300_000)))
        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAssetId, BigDecimal.valueOf(100)))

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.assetPairId,
                BigDecimal.valueOf(-0.01),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT2,
                ASSET_PAIR.assetPairId,
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(5500),
                false))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.assetPairId,
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT2,
                ASSET_PAIR.assetPairId,
                BigDecimal.valueOf(-0.01),
                BigDecimal.valueOf(5500),
                false))
                .forEach(meClient::sendMessage)
    }

    fun testOrderFullyMatchedSeveralLevels() {
        cancelAllClientOrders(assetPairId =  ASSET_PAIR.assetPairId)

        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAssetId, BigDecimal.valueOf(-300_000)))
        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAssetId, BigDecimal.valueOf(-100)))
        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAssetId, BigDecimal.valueOf(300_000)))
        meClient.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAssetId, BigDecimal.valueOf(100)))

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.assetPairId,
                BigDecimal.valueOf(-0.1),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!! / 5, getStrategy(CLIENT2,
                ASSET_PAIR.assetPairId,
                BigDecimal.valueOf(0.5),
                BigDecimal.valueOf(5500),
                false,
                0.5))
                .forEach(meClient::sendMessage)
    }

    fun testOrderPartiallyMatched() {
        cancelAllClientOrders( assetPairId =  ASSET_PAIR.assetPairId)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.assetPairId,
                BigDecimal.valueOf(-0.1),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!! / 5, getStrategy(CLIENT2,
                ASSET_PAIR.assetPairId,
                BigDecimal.valueOf(0.5),
                BigDecimal.valueOf(5500),
                false,
                0.4))
                .forEach(meClient::sendMessage)
    }

    private fun getStrategy(client: String,
                            assetPairId: String,
                            volume: BigDecimal,
                            price: BigDecimal,
                            incrementPrice: Boolean = true,
                            delta: Double = 0.1): (Int) -> Message {
        var priceDelta = AtomicDouble(0.0)
        return { idx ->
            if (incrementPrice) {
                priceDelta.addAndGet(delta)
            } else {
                priceDelta.addAndGet(-delta)
            }
            messageBuilder.buildLimitOrderMessage(client, assetPairId, volume, price.plus(BigDecimal.valueOf(priceDelta.get())))
        }
    }

    private fun cancelAllClientOrders(clientId: String? = null, assetPairId: String? = null) {
        meClient.sendMessage(messageBuilder.buildLimitOrderMassCancelMessage(clientId, assetPairId))
    }
}