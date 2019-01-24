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
class CashTransferTest {
    private companion object {
        val MESSAGE_COUNT = 10_000
    }

    @Autowired
    private lateinit var client: MeClient

    @Autowired
    private lateinit var messageBuilder: MessageBuilder

    fun trasferFromClient1ToClient2() {
        client.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, "BTC", BigDecimal.valueOf(1000.0)))
        generateMessages(MESSAGE_COUNT, getStrategy(CLIENT1, CLIENT2, "BTC", BigDecimal.valueOf(0.1))).forEach(client::sendMessage)
    }


    fun getStrategy(from: String, to: String, assetId: String, volume: BigDecimal): () -> Message {
        return {
            messageBuilder.buildCashTransferMessage(from, to, assetId,
                    fee = SimpleFeeInstruction(FeeType.PERCENTAGE, FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.1), CLIENT1, CLIENT3, listOf(assetId)),
                    volume = volume)
        }
    }
}