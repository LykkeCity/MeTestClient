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

    @Autowired
    private lateinit var meClient: MeClient

    @Autowired
    private lateinit var messageBuilder: MessageBuilder

    fun cashInTest() {
        val result = ArrayList<Message>(20_000)
        result.addAll(generateCashInMessages(10000, CLIENT1, "BTC", BigDecimal.valueOf(0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("BTC")))))
        result.addAll(generateCashInMessages(10000, CLIENT2, "USD", BigDecimal.valueOf(0.1), listOf(SimpleFeeInstruction(FeeType.PERCENTAGE,
                FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.01), CLIENT1, CLIENT3, listOf("USD")))))

        meClient.sendMessages(result)
    }

    fun cashOutTest() {
        val result = ArrayList<Message>(20_000)
        result.addAll(generateCashInMessages(10000, CLIENT1, "BTC", BigDecimal.valueOf(-0.1)))
        result.addAll(generateCashInMessages(10000, CLIENT2, "USD", BigDecimal.valueOf(-0.1)))

        meClient.sendMessages(result)
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