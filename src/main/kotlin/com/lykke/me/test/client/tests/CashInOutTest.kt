package com.lykke.me.test.client.tests

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.outgoing.messages.common.FeeSizeType
import com.lykke.me.test.client.outgoing.messages.common.FeeType
import com.lykke.me.test.client.outgoing.messages.common.SimpleFeeInstruction
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

@MeTest
class CashInOutTest {
    private companion object {
        val TEST_RUN_COUNT = 10_000
    }

    @Autowired
    private lateinit var meClient: MeClient

    @Autowired
    private lateinit var messageBuilder: MessageBuilder

    fun cashInTest() {
        val result = ArrayList<Message>(TEST_RUN_COUNT * 2)
        result.addAll(generateCashInMessages(TEST_RUN_COUNT, CLIENT1, "BTC", BigDecimal.valueOf(0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("BTC")))))
        result.addAll(generateCashInMessages(TEST_RUN_COUNT, CLIENT2, "USD", BigDecimal.valueOf(0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("USD")))))

        result.forEach(meClient::sendMessage)
    }

    fun cashOutTest() {
        val result = ArrayList<Message>(TEST_RUN_COUNT * 2)
        result.addAll(generateCashInMessages(TEST_RUN_COUNT, CLIENT1, "BTC", BigDecimal.valueOf(-0.1)))
        result.addAll(generateCashInMessages(TEST_RUN_COUNT, CLIENT2, "USD", BigDecimal.valueOf(-0.1)))

        result.forEach(meClient::sendMessage)
    }

    fun cashInInputValidationFailed() {
        generateCashInMessages(TEST_RUN_COUNT, CLIENT1, "BTC", BigDecimal.valueOf(0.000000001), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("BTC")))).forEach(meClient::sendMessage)
    }


    fun cashInBusinessValidationFailed() {
        generateCashInMessages(TEST_RUN_COUNT, CLIENT1, "BTC", BigDecimal.valueOf(-0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("BTC")))).forEach(meClient::sendMessage)
    }


    private fun generateCashInMessages(count: Int,
                                       clientId: String,
                                       assetId: String,
                                       volume: BigDecimal,
                                       fees: List<SimpleFeeInstruction> = emptyList()): List<Message> {
        val listOfMessages = ArrayList<Message>()
        for (i in IntRange(0, count)) {
            listOfMessages.add(messageBuilder.buildCashInOutMessage(clientId, assetId, volume, fees))
        }
        return listOfMessages
    }
}