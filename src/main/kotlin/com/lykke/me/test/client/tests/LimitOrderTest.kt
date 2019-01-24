package com.lykke.me.test.client.tests

import com.google.common.util.concurrent.AtomicDouble
import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import com.lykke.me.test.client.utils.generateMessages
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import javax.annotation.PostConstruct

@MeTest(3)
class LimitOrderTest {
    private companion object {
        val MESSAGES_COUNT = 10_000
        val ASSET_PAIR_ID = "BTCUSD"
    }

    @Autowired
    private lateinit var meClient: MeClient

    @Autowired
    private lateinit var messageBuilder: MessageBuilder

    @Autowired
    private lateinit var config: Config

    private lateinit var CLIENT1: String
    private lateinit var CLIENT2: String

    @PostConstruct
    fun init() {
        CLIENT1 = config.matchingEngineTestClient.testPrerequisitesConfig.clientsConfig.clients.toList()[0]
        CLIENT2 = config.matchingEngineTestClient.testPrerequisitesConfig.clientsConfig.clients.toList()[1]
    }

    fun testOrderNotMatch() {
        cancelAllClientOrders(CLIENT1, ASSET_PAIR_ID)

        generateMessages(MESSAGES_COUNT, getStrategy(CLIENT1,
                ASSET_PAIR_ID,
                BigDecimal.valueOf(0.1),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)
    }

    fun testOrderFullyMatchedOneLevel() {
        cancelAllClientOrders(CLIENT1, ASSET_PAIR_ID)
        cancelAllClientOrders(CLIENT2, ASSET_PAIR_ID)

        generateMessages(MESSAGES_COUNT / 2, getStrategy(CLIENT1,
                ASSET_PAIR_ID,
                BigDecimal.valueOf(-0.1),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)

        generateMessages(MESSAGES_COUNT / 2, getStrategy(CLIENT2,
                ASSET_PAIR_ID,
                BigDecimal.valueOf(0.1),
                BigDecimal.valueOf(5500),
                false))
                .forEach(meClient::sendMessage)

        generateMessages(MESSAGES_COUNT / 2, getStrategy(CLIENT1,
                ASSET_PAIR_ID,
                BigDecimal.valueOf(0.1),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)

        generateMessages(MESSAGES_COUNT / 2, getStrategy(CLIENT2,
                ASSET_PAIR_ID,
                BigDecimal.valueOf(-0.1),
                BigDecimal.valueOf(5500),
                false))
                .forEach(meClient::sendMessage)
    }

    fun testOrderFullyMatchedSeveralLevels() {
        cancelAllClientOrders(CLIENT1, ASSET_PAIR_ID)
        cancelAllClientOrders(CLIENT2, ASSET_PAIR_ID)

        generateMessages(MESSAGES_COUNT, getStrategy(CLIENT1,
                ASSET_PAIR_ID,
                BigDecimal.valueOf(-0.1),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)

        generateMessages(MESSAGES_COUNT / 5, getStrategy(CLIENT2,
                ASSET_PAIR_ID,
                BigDecimal.valueOf(0.5),
                BigDecimal.valueOf(5500),
                false,
                0.5))
                .forEach(meClient::sendMessage)
    }

    fun testOrderPartiallyMatched() {
        cancelAllClientOrders(CLIENT1, ASSET_PAIR_ID)
        cancelAllClientOrders(CLIENT2, ASSET_PAIR_ID)

        generateMessages(MESSAGES_COUNT, getStrategy(CLIENT1,
                ASSET_PAIR_ID,
                BigDecimal.valueOf(-0.1),
                BigDecimal.valueOf(5500)))
                .forEach(meClient::sendMessage)

        generateMessages(MESSAGES_COUNT / 5, getStrategy(CLIENT2,
                ASSET_PAIR_ID,
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

    private fun cancelAllClientOrders(clientId: String, assetPairId: String? = null) {
        meClient.sendMessage(messageBuilder.buildLimitOrderMassCancelMessage(clientId, assetPairId))
    }
}