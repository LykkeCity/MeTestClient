package com.lykke.me.test.client.tests

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.common.FeeSizeType
import com.lykke.me.test.client.outgoing.messages.common.FeeType
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import com.lykke.me.test.client.utils.generateMessages
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import javax.annotation.PostConstruct

@MeTest
@TestGroup("CashInOutTest")
class CashInOutTest {
    private companion object {
        val TEST_RUN_COUNT = 10_000
    }

    @Autowired
    private lateinit var messageBuilder: MessageBuilder

    @Autowired
    private lateinit var config: Config

    private lateinit var meClient: MeClient

    private lateinit var CLIENT1: String

    private lateinit var CLIENT2: String

    private lateinit var CLIENT3: String

    private lateinit var ASSET1: String

    private lateinit var ASSET2: String

    @PostConstruct
    private fun init() {

        val availableClients = config.matchingEngineTestClient.testPrerequisitesConfig.clientsConfig.clients.toList()
        val assets = config.matchingEngineTestClient.testPrerequisitesConfig.assetsConfig.toList()[0]
        CLIENT1 = availableClients[0]
        CLIENT2 = availableClients[1]
        CLIENT3 = availableClients[2]
        ASSET1 = assets.baseAssetId
        ASSET2 = assets.quotingAssetId
    }

    fun cashInTest() {
        val result = ArrayList<Message>(TEST_RUN_COUNT * 2)
        result.addAll(generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT1, ASSET1, BigDecimal.valueOf(0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf(ASSET1))))))

        result.addAll(generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT2, ASSET2, BigDecimal.valueOf(0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf(ASSET2))))))

        result.forEach(meClient::sendMessage)
    }

    fun cashOutTest() {
        val result = ArrayList<Message>(TEST_RUN_COUNT * 2)
        result.addAll(generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT1, ASSET1, BigDecimal.valueOf(-0.1))))
        result.addAll(generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT2, ASSET2, BigDecimal.valueOf(-0.1))))

        result.forEach(meClient::sendMessage)
    }

    fun cashInInputValidationFailed() {
        generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT1, ASSET1, BigDecimal.valueOf(0.000000001), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf(ASSET1))))).forEach(meClient::sendMessage)
    }


    fun cashInBusinessValidationFailed() {
        generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT1, ASSET1, BigDecimal.valueOf(-0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf(ASSET1))))).forEach(meClient::sendMessage)
    }


    private fun getStrategy(
            clientId: String,
            assetId: String,
            volume: BigDecimal,
            fees: List<SimpleFeeInstruction> = emptyList()): (Int) -> Message {
        return { messageBuilder.buildCashInOutMessage(clientId, assetId, volume, fees) }
    }
}