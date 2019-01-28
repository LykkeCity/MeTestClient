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
class CashTransferTest {
    private companion object {
        val MESSAGE_COUNT = 10_000
    }

    private lateinit var CLIENT1: String

    private lateinit var CLIENT2: String

    private lateinit var CLIENT3: String

    private lateinit var client: MeClient

    @Autowired
    private lateinit var config: Config

    @Autowired
    private lateinit var messageBuilder: MessageBuilder


    @PostConstruct
    private fun init() {
        val availableClients = config.matchingEngineTestClient.testPrerequisitesConfig.clientsConfig.clients.toList()
        CLIENT1 = availableClients[0]
        CLIENT1 = availableClients[1]
        CLIENT3 = availableClients[2]
    }

    fun trasferFromClient1ToClient2() {
        client.sendMessage(messageBuilder.buildCashInOutMessage(CLIENT1, "BTC", BigDecimal.valueOf(1000.0)))
        generateMessages(MESSAGE_COUNT, getStrategy(CLIENT1, CLIENT2, "BTC", BigDecimal.valueOf(0.1))).forEach(client::sendMessage)
    }


    private fun getStrategy(from: String, to: String, assetId: String, volume: BigDecimal): (Int) -> Message {
        return {
            messageBuilder.buildCashTransferMessage(from, to, assetId,
                    fee = SimpleFeeInstruction(FeeType.PERCENTAGE, FeeSizeType.CLIENT_FEE, BigDecimal.valueOf(0.1), CLIENT1, CLIENT3, listOf(assetId)),
                    volume = volume)
        }
    }
}