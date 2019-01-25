package com.lykke.me.test.client.tests

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.common.FeeSizeType
import com.lykke.me.test.client.outgoing.messages.common.FeeType
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import com.lykke.me.test.client.utils.generateMessages
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

@MeTest
class CashInOutTest {
    private companion object {
        val TEST_RUN_COUNT = 10_000
    }

    private lateinit var meClient: MeClient

    @Autowired
    private lateinit var messageBuilder: MessageBuilder

    fun cashInTest() {
        val result = ArrayList<Message>(TEST_RUN_COUNT * 2)
        result.addAll(generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT1, "BTC", BigDecimal.valueOf(0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("BTC"))))))

        result.addAll(generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT2, "USD", BigDecimal.valueOf(0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("USD"))))))

        result.forEach(meClient::sendMessage)
    }

    fun cashOutTest() {
        val result = ArrayList<Message>(TEST_RUN_COUNT * 2)
        result.addAll(generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT1, "BTC", BigDecimal.valueOf(-0.1))))
        result.addAll(generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT2, "USD", BigDecimal.valueOf(-0.1))))

        result.forEach(meClient::sendMessage)
    }

    fun cashInInputValidationFailed() {
        generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT1, "BTC", BigDecimal.valueOf(0.000000001), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("BTC"))))).forEach(meClient::sendMessage)
    }


    fun cashInBusinessValidationFailed() {
        generateMessages(TEST_RUN_COUNT, getStrategy(CLIENT1, "BTC", BigDecimal.valueOf(-0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("BTC"))))).forEach(meClient::sendMessage)
    }


    private fun getStrategy(
            clientId: String,
            assetId: String,
            volume: BigDecimal,
            fees: List<SimpleFeeInstruction> = emptyList()): () -> Message {
        return { messageBuilder.buildCashInOutMessage(clientId, assetId, volume, fees) }
    }
}