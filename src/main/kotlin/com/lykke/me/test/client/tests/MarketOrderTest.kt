package com.lykke.me.test.client.tests

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.config.MarketOrderTestConfig
import com.lykke.me.test.client.entity.Asset
import com.lykke.me.test.client.entity.AssetPair
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
    private lateinit var ASSET_PAIR1: AssetPair
    private lateinit var MARKET_ORDER_TEST_CONFIG: MarketOrderTestConfig
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

        MARKET_ORDER_TEST_CONFIG = config.matchingEngineTestClient.marketOrderTestConfig
        val configAssetPair = config.matchingEngineTestClient.testPrerequisitesConfig.assetsConfig.toList()[0]
        ASSET_PAIR1 = AssetPair(configAssetPair.assetPairId, configAssetPair.assetPairAccuracy,
                Asset(configAssetPair.baseAssetId, configAssetPair.baseAssetAccuracy),
                Asset(configAssetPair.quotingAssetId, configAssetPair.quotingAssetAccuracy))
        MAX_ORDERS_IN_ORDER_BOOK = config.matchingEngineTestClient.testPrerequisitesConfig.maxOrdersInOrderBook
    }

    fun testFullMatchOneLevel() {
        prepareEnv()

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getLimitOrderStrategy(CLIENT1,
                ASSET_PAIR1.id,
                MARKET_ORDER_TEST_CONFIG.volume.negate(),
                MARKET_ORDER_TEST_CONFIG.startAskPrice))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getMarketOrderStrategy(CLIENT2,
                ASSET_PAIR1.id,
                MARKET_ORDER_TEST_CONFIG.volume))
                .forEach(meClient::sendMessage)
    }

    fun testFullMatchSeveralLevelsMatched() {
        prepareEnv()

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getLimitOrderStrategy(CLIENT1,
                ASSET_PAIR1.id,
                MARKET_ORDER_TEST_CONFIG.volume.negate(),
                MARKET_ORDER_TEST_CONFIG.startAskPrice))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!! / 5, getMarketOrderStrategy(CLIENT2,
                ASSET_PAIR1.id,
                BigDecimal.valueOf(0.05)))
                .forEach(meClient::sendMessage)
    }

    fun testFullMatchSeveralLevelsMatchedPartialMatchLimitOrder() {
        prepareEnv()

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getLimitOrderStrategy(CLIENT1,
                ASSET_PAIR1.id,
                MARKET_ORDER_TEST_CONFIG.volume.negate(),
                MARKET_ORDER_TEST_CONFIG.startAskPrice))
                .forEach(meClient::sendMessage)

        generateMessages((MAX_ORDERS_IN_ORDER_BOOK!! * MARKET_ORDER_TEST_CONFIG.volume.toInt() / 0.054).toInt(), getMarketOrderStrategy(CLIENT2,
                ASSET_PAIR1.id,
                BigDecimal.valueOf(0.054)))
    }

    private fun prepareEnv() {
        meClientForSyncInteraction.sendMessage(messageBuilder.buildLimitOrderMassCancelMessage(assetPairId = ASSET_PAIR1.id))

        val foundsNeededBaseAsset = calculateFundsNeeded(BigDecimal.valueOf(MAX_ORDERS_IN_ORDER_BOOK!!.toLong()), MARKET_ORDER_TEST_CONFIG.startAskPrice.multiply(MARKET_ORDER_TEST_CONFIG.volume),
                MARKET_ORDER_TEST_CONFIG.volume.multiply(MARKET_ORDER_TEST_CONFIG.priceStep))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR1.baseAsset.id,
                foundsNeededBaseAsset.negate()))

        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR1.baseAsset.id,
                foundsNeededBaseAsset))

        val fundsNeededQuotingAsset = calculateFundsNeeded(BigDecimal.valueOf(MAX_ORDERS_IN_ORDER_BOOK!!.toLong()), MARKET_ORDER_TEST_CONFIG.startBidPrice, BigDecimal.ZERO)
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR1.quotingAsset.id,
                fundsNeededQuotingAsset.negate()))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR1.quotingAsset.id,
                fundsNeededQuotingAsset))
    }

    private fun getMarketOrderStrategy(client: String,
                                       assetPairId: String,
                                       volume: BigDecimal): (Int) -> Message {
        return { idx -> messageBuilder.buildMarketOrderMessage(client, assetPairId, volume) }
    }


    private fun getLimitOrderStrategy(client: String,
                                      assetPairId: String,
                                      volume: BigDecimal,
                                      price: BigDecimal,
                                      incrementPrice: Boolean = true,
                                      delta: BigDecimal = BigDecimal.valueOf(0.1)): (Int) -> Message {
        var priceDelta = ArrayList<BigDecimal>(listOf(delta))

        return { idx ->
            if (incrementPrice) {
                priceDelta[0] = priceDelta.first().add(delta)
            } else {
                priceDelta[0] = priceDelta.first().subtract(delta)
            }
            messageBuilder.buildLimitOrderMessage(client, assetPairId, volume, price.plus(priceDelta.first()))
        }
    }
}