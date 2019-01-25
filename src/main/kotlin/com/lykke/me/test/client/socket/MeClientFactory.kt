package com.lykke.me.test.client.socket

import com.lykke.me.test.client.MeBlockingClient
import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.service.MessageRatePolicy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MeClientFactory {

    @Autowired
    private lateinit var meClient: MeClient

    @Autowired
    private lateinit var meSocketProtoBlockingClient: MeBlockingClient

    fun getClient(messageRatePolicy: MessageRatePolicy): MeClient {
        return when (messageRatePolicy) {
            MessageRatePolicy.AUTO_MESSAGE_RATE -> meSocketProtoBlockingClient
            MessageRatePolicy.MANUAL_MESSAGE_RATE -> meClient
        }
    }
}