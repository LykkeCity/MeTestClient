package com.lykke.me.test.client.rabbitmq

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import java.util.concurrent.BlockingQueue

class MeEventConsumer(channel: Channel,
                      private val routingKey: String,
                      private val queue: BlockingQueue<RmqMessageWrapper>): DefaultConsumer(channel) {

    override fun handleDelivery(consumerTag: String?, envelope: Envelope?, properties: AMQP.BasicProperties?, body: ByteArray) {
        queue.put(RmqMessageWrapper(routingKey, body))
    }
}