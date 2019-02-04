package com.lykke.me.test.client.tests

import com.lykke.me.test.client.MeBlockingClient
import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.config.LimitOrderTestConfig
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
@TestGroup("LimitOrderTest")
class LimitOrderTest {

    private lateinit var meClient: MeClient

    @Autowired
    private lateinit var messageBuilder: MessageBuilder

    @Autowired
    private lateinit var config: Config

    @Autowired
    private lateinit var meClientForSyncInteraction: MeBlockingClient

    private lateinit var CLIENT1: String
    private lateinit var CLIENT2: String
    private lateinit var ASSET_PAIR: AssetPair
    private lateinit var LIMIT_ORDER_TEST_CONFIG: LimitOrderTestConfig
    private var MAX_ORDERS_IN_ORDER_BOOK: Int? = null

    @PostConstruct
    private fun init() {
        CLIENT1 = config.matchingEngineTestClient.testPrerequisitesConfig.clientsConfig.clients.toList()[0]
        CLIENT2 = config.matchingEngineTestClient.testPrerequisitesConfig.clientsConfig.clients.toList()[1]
        val assetPairConfig = config.matchingEngineTestClient.testPrerequisitesConfig.assetsConfig.toList()[2]
        ASSET_PAIR = AssetPair(assetPairConfig.assetPairId, assetPairConfig.assetPairAccuracy,
                Asset(assetPairConfig.baseAssetId, assetPairConfig.baseAssetAccuracy),
                Asset(assetPairConfig.quotingAssetId, assetPairConfig.quotingAssetAccuracy))
        MAX_ORDERS_IN_ORDER_BOOK = config.matchingEngineTestClient.testPrerequisitesConfig.maxOrdersInOrderBook
        LIMIT_ORDER_TEST_CONFIG = config.matchingEngineTestClient.limitOrderTestConfig
    }

    fun testOrderNotMatch() {
        cancelAllClientOrders(assetPairId = ASSET_PAIR.id)
        val fundsNeededUSD = calculateBuyFundsNeeded()
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.quotingAsset.id, fundsNeededUSD.negate()))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.quotingAsset.id, fundsNeededUSD))

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.id,
                LIMIT_ORDER_TEST_CONFIG.volume,
                LIMIT_ORDER_TEST_CONFIG.startBidPrice))
                .forEach(meClient::sendMessage)
    }

    fun testOrderFullyMatchedOneLevel() {
        cancelAllClientOrders(assetPairId = ASSET_PAIR.id)
        val fundsNeededUSD = calculateBuyFundsNeeded()
        val fundsNeededBTC = calculateSellFundsNeeded()

        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAsset.id, fundsNeededUSD.negate()))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAsset.id, fundsNeededBTC.negate()))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAsset.id, fundsNeededUSD))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAsset.id, fundsNeededBTC))

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.id,
                LIMIT_ORDER_TEST_CONFIG.volume.negate(),
                LIMIT_ORDER_TEST_CONFIG.startAskPrice))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT2,
                ASSET_PAIR.id,
                LIMIT_ORDER_TEST_CONFIG.volume,
                LIMIT_ORDER_TEST_CONFIG.startBidPrice))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.id,
                LIMIT_ORDER_TEST_CONFIG.volume,
                LIMIT_ORDER_TEST_CONFIG.startBidPrice))
                .forEach(meClient::sendMessage)

        val generateMessages = generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT2,
                ASSET_PAIR.id,
                LIMIT_ORDER_TEST_CONFIG.volume.negate(),
                LIMIT_ORDER_TEST_CONFIG.startAskPrice, false))
        generateMessages
                .forEach(meClient::sendMessage)
    }

    fun testOrderFullyMatchedSeveralLevels() {
        cancelAllClientOrders(assetPairId = ASSET_PAIR.id)
        val fundsNeededUSD = calculateBuyFundsNeeded()
        val fundsNeededBTC = calculateSellFundsNeeded()

        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAsset.id, fundsNeededUSD.negate()))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAsset.id, fundsNeededBTC.negate()))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAsset.id, fundsNeededUSD))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAsset.id, fundsNeededBTC))

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.id,
                LIMIT_ORDER_TEST_CONFIG.volume.negate(),
                LIMIT_ORDER_TEST_CONFIG.startAskPrice))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!! / 5, getStrategy(CLIENT2,
                ASSET_PAIR.id,
                BigDecimal.valueOf(0.5),
                LIMIT_ORDER_TEST_CONFIG.startBidPrice,
                true,
                BigDecimal.valueOf(0.5)))
                .forEach(meClient::sendMessage)
    }

    fun testOrderPartiallyMatched() {
        cancelAllClientOrders(assetPairId = ASSET_PAIR.id)

        val fundsNeededUSD = calculateFundsNeeded(BigDecimal.valueOf(MAX_ORDERS_IN_ORDER_BOOK!!.toLong()),
                LIMIT_ORDER_TEST_CONFIG.startBidPrice.multiply(LIMIT_ORDER_TEST_CONFIG.volume), LIMIT_ORDER_TEST_CONFIG.priceStep.multiply(LIMIT_ORDER_TEST_CONFIG.volume))
        val fundsNeededBTC = calculateSellFundsNeeded()

        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAsset.id, fundsNeededUSD.negate()))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAsset.id, fundsNeededBTC.negate()))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT2, ASSET_PAIR.quotingAsset.id, fundsNeededUSD))
        meClientForSyncInteraction.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, ASSET_PAIR.baseAsset.id, fundsNeededBTC))

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!!, getStrategy(CLIENT1,
                ASSET_PAIR.id,
                LIMIT_ORDER_TEST_CONFIG.volume.negate(),
                LIMIT_ORDER_TEST_CONFIG.startAskPrice))
                .forEach(meClient::sendMessage)

        generateMessages(MAX_ORDERS_IN_ORDER_BOOK!! / 5, getStrategy(CLIENT2,
                ASSET_PAIR.id,
                BigDecimal.valueOf(0.5),
                LIMIT_ORDER_TEST_CONFIG.startBidPrice,
                true,
                BigDecimal.valueOf(0.4)))
                .forEach(meClient::sendMessage)
    }

    private fun calculateBuyFundsNeeded(): BigDecimal {
        return calculateFundsNeeded(BigDecimal.valueOf(MAX_ORDERS_IN_ORDER_BOOK!!.toLong()),
                LIMIT_ORDER_TEST_CONFIG.startBidPrice.multiply(LIMIT_ORDER_TEST_CONFIG.volume), LIMIT_ORDER_TEST_CONFIG.priceStep.multiply(LIMIT_ORDER_TEST_CONFIG.volume))
    }

    private fun calculateSellFundsNeeded(): BigDecimal {
        return calculateFundsNeeded(BigDecimal.valueOf(MAX_ORDERS_IN_ORDER_BOOK!!.toLong()), LIMIT_ORDER_TEST_CONFIG.volume, BigDecimal.ZERO)
    }

    private fun getStrategy(client: String,
                            assetPairId: String,
                            volume: BigDecimal,
                            price: BigDecimal,
                            incrementPrice: Boolean = true,
                            delta: BigDecimal = BigDecimal.valueOf(0.1)): (Int) -> Message {
        var priceDelta = ArrayList<BigDecimal>(listOf(BigDecimal.ZERO))
        return { idx ->
            if (incrementPrice) {
                priceDelta[0] =  priceDelta.first().add(delta)
            } else {
                priceDelta[0] = priceDelta.first().subtract(delta)
            }
            messageBuilder.buildLimitOrderMessage(client, assetPairId, volume, price.plus(priceDelta[0]))
        }
    }

    private fun cancelAllClientOrders(clientId: String? = null, assetPairId: String? = null) {
        meClientForSyncInteraction.sendMessageSync(messageBuilder.buildLimitOrderMassCancelMessage(clientId, assetPairId))
    }
}