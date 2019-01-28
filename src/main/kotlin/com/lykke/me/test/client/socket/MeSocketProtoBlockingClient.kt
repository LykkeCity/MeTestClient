package com.lykke.me.test.client.socket

import com.lykke.me.test.client.MeBlockingClient
import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.MeSubscriber
import com.lykke.me.test.client.incoming.response.Response
import com.lykke.me.test.client.outgoing.messages.Message
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.PostConstruct

class MeSocketProtoBlockingClient(requestResponseCountThreshold: Int): MeBlockingClient {

    companion object {
        private val LOGGER = Logger.getLogger(MeSocketProtoBlockingClient::class.java.name)
    }

    @Autowired
    private lateinit var meClient: MeClient

    @Autowired
    private lateinit var responseListener: MeSocketProtoResponseListener

    @Autowired
    private lateinit var meClientForSyncInteraction: MeClient

    @Autowired
    private lateinit var meResponseListenerForSyncInteraction: MeSocketProtoResponseListener

    private var messagesSend =  AtomicLong(0)
    private var messageSyncSend = AtomicLong(0)

    private val sendMessagesLatch = SendMessagesLatch(requestResponseCountThreshold)
    private val sendMessagesLatchForSync = SendMessagesLatch(0)

    @PostConstruct
    fun init() {
        responseListener.subscribe(object : MeSubscriber<Response> {
            override fun notify(message: Response) {
                sendMessagesLatch.incrementResponseCount()
            }
        })

        meResponseListenerForSyncInteraction.subscribe(object : MeSubscriber<Response> {
            override fun notify(message: Response) {
                sendMessagesLatchForSync.incrementResponseCount()
            }
        })
    }

    override fun sendMessageSync(message: Message) {
        meClientForSyncInteraction.sendMessage(message)
        messageSyncSend.incrementAndGet()
        sendMessagesLatchForSync.await(messagesSend.get())
    }

    override fun sendMessage(message: Message) {
        LOGGER.trace("Pause sending requests to ME: messages send: ${messagesSend.get()}, responses: ${sendMessagesLatch.getResponsesReceived()}")
        sendMessagesLatch.await(messagesSend.get())
        LOGGER.trace("Continue to send requests to ME: messages send: ${messagesSend.get()}, responses: ${sendMessagesLatch.getResponsesReceived()}")
        meClient.sendMessage(message)
        messagesSend.incrementAndGet()
    }
}