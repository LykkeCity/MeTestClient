package com.lykke.me.test.client.tests

import com.google.common.util.concurrent.AtomicDouble
import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.config.TestPrerequisitesConfig
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import com.lykke.me.test.client.utils.calculateFundsNeeded
import com.lykke.me.test.client.utils.generateMessages
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import javax.annotation.PostConstruct

@MeTest
class MarketOrderTest {

    private lateinit var meClient: MeClient

    private lateinit var CLIENT1: String
    private lateinit var CLIENT2: String
    private lateinit var ASSET_PAIR1: TestPrerequisitesConfig.AssetPairConfig
    private var MAX_ORDERS_IN_ORDER_BOOK: Int? = null

    @Autowired
    private lateinit var config: Config

    @Autowired
    private lateinit var meClientForSyncInteraction: MeClient

    @Autowired
    private lateinit var messageBuilder: MessageBuilder

    @PostConstruct
    private fun init() {
        val clients = config.matchingEngineTestClient.testPrerequisitesConfig.clientsConfig.clients.toList()
        CLIENT1 = clients[0]
        CLIENT2 = clients[1]

        ASSET_PAIR1 = config.matchingEngineTestClient.testPrerequisitesConfig.assetsConfig.toList()[0]
        MAX_ORDERS_IN_ORDER_BOOK = config.matchingEngineTestClient.testPrerequisitesConfig.maxOrdersInOrderBook
    }

    fun testFullMatchOneLevel() {
        prepareEnv()

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getLimitOrderStrategy(CLIENT1,
                ASSET_PAIR1.assetPairId,
                BigDecimal.valueOf(-0.01),
                BigDecimal.valueOf(5500.0)))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getMarketOrderStrategy(CLIENT2,
                ASSET_PAIR1.assetPairId,
        BigDecimal.valueOf(0.01)))
                .forEach (meClient::sendMessage)
    }

    fun testFullMatchSeveralLevelsMatched() {
        prepareEnv()

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getLimitOrderStrategy(CLIENT1,
                ASSET_PAIR1.assetPairId,
                BigDecimal.valueOf(-0.01),
                BigDecimal.valueOf(5500.0)))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!! / 5, getMarketOrderStrategy(CLIENT2,
                ASSET_PAIR1.assetPairId,
                BigDecimal.valueOf(0.05)))
                .forEach (meClient::sendMessage)
    }

    fun testFullMatchSeveralLevelsMatchedPartialMatchLimitOrder() {
        prepareEnv()

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getLimitOrderStrategy(CLIENT1,
                ASSET_PAIR1.assetPairId,
                BigDecimal.valueOf(-0.01),
                BigDecimal.valueOf(5500.0)))
                .forEach(meClient::sendMessage)

        generateMessages((MAX_ORDERS_IN_ORDER_BOOK!! * 0.01 / 0.054).toInt(), getMarketOrderStrategy(CLIENT2,
                ASSET_PAIR1.assetPairId,
                BigDecimal.valueOf(0.054)))
    }

    fun prepareEnv() {
        meClientForSyncInteraction.sendMessage(messageBuilder.buildLimitOrderMassCancelMessage(assetPairId = ASSET_PAIR1.assetPairId))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR1.baseAssetId,
                calculateFundsNeeded(BigDecimal.valueOf(MAX_ORDERS_IN_ORDER_BOOK!!.toLong()), BigDecimal.valueOf(-5500 * 0.01), BigDecimal.valueOf(0.1 * 0.01))))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR1.baseAssetId,
                calculateFundsNeeded(BigDecimal.valueOf(MAX_ORDERS_IN_ORDER_BOOK!!.toLong()), BigDecimal.valueOf(5500 * 0.01), BigDecimal.valueOf(0.1 * 0.01))))

        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR1.quotingAssetId,
                calculateFundsNeeded(BigDecimal.valueOf(MAX_ORDERS_IN_ORDER_BOOK!!.toLong()), BigDecimal.valueOf(-5500), BigDecimal.ZERO)))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR1.quotingAssetId,
                calculateFundsNeeded(BigDecimal.valueOf(MAX_ORDERS_IN_ORDER_BOOK!!.toLong()), BigDecimal.valueOf(5500), BigDecimal.ZERO)))
    }

    private fun getMarketOrderStrategy(client: String,
                                       assetPairId: String,
                                       volume: BigDecimal): (Int) -> Message {
        return { idx -> messageBuilder.buildMarketOrderMessage(client, assetPairId, volume)}
    }



    private fun getLimitOrderStrategy(client: String,
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
}