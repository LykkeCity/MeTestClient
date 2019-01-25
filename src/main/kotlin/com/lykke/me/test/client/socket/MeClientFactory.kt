package com.lykke.me.test.client.socket

import com.lykke.me.test.client.MeBlockingClient
import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.outgoing.messages.Message
import com.lykke.me.test.client.service.MessageRatePolicy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class MeClientFactory {

    @Autowired
    private lateinit var meClient: MeClient

    @Autowired
    private lateinit var meSocketProtoBlockingClient: MeBlockingClient

    fun getClient(messageRatePolicy: MessageRatePolicy,
                  messageDelayMs: Long?): MeClient {
        if (messageRatePolicy == MessageRatePolicy.MANUAL_MESSAGE_RATE &&
                messageDelayMs == null) {
            throw IllegalArgumentException("Manual message rate mode requires 'messageDelayMs' parameter")
        }

        return when (messageRatePolicy) {
            MessageRatePolicy.AUTO_MESSAGE_RATE -> meSocketProtoBlockingClient
            MessageRatePolicy.MANUAL_MESSAGE_RATE -> object : MeClient {
                override fun sendMessage(message: Message) {
                    Thread.sleep(messageDelayMs!!)
                    meClient.sendMessage(message)
                }
            }
        }
    }
}