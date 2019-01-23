package com.lykke.me.test.client.rabbitmq

import com.lykke.me.test.client.AbstractMeListener
import com.lykke.me.test.client.config.RabbitMqConfig
import com.lykke.me.test.client.incoming.events.proto.MeProtoEvent
import com.lykke.me.test.client.incoming.events.proto.deserialization.MeProtoEventDeserializer
import com.lykke.utils.rabbit.Connector
import com.lykke.utils.rabbit.ConsumerFactory
import com.lykke.utils.rabbit.RabbitMqSubscriber
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Consumer
import org.apache.log4j.Logger
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class MeRabbitMqProtoEventListener(private val configs: Set<RabbitMqConfig>)
    : AbstractMeListener<MeProtoEvent<*>>() {

    companion object {
        private val LOGGER = Logger.getLogger(MeRabbitMqProtoEventListener::class.java.name)
    }

    private val queue = LinkedBlockingQueue<RmqMessageWrapper>()
    private val deserializerByRoutingKey = HashMap<String, MeProtoEventDeserializer<*>>()

    private fun start() {
        initDeserializers()
        startRabbitMqSubscribers()
        startMessagesProcessing()
    }

    private fun initDeserializers() {
        deserializerByRoutingKey.putAll(configs.map { it.routingKey }
                .groupBy { it }
                .mapValues { MeProtoEventDeserializer.createDeserializer(it.value.single()) })
    }

    private fun startRabbitMqSubscribers() {
        configs.forEach(::startRabbitMqSubscriber)
    }

    private fun startRabbitMqSubscriber(config: RabbitMqConfig) {
        val tmpFactory = ConnectionFactory()
        tmpFactory.setUri(config.uri)
        val routingKey = config.routingKey
        RabbitMqSubscriber(
                com.lykke.utils.rabbit.RabbitMqConfig(tmpFactory.host,
                        tmpFactory.port,
                        tmpFactory.username,
                        tmpFactory.password,
                        config.exchange,
                        config.queue,
                        null),
                object : Connector {
                    override fun createChannel(config: com.lykke.utils.rabbit.RabbitMqConfig): Channel {
                        val factory = ConnectionFactory()
                        factory.host = config.host
                        factory.port = config.port
                        factory.username = config.username
                        factory.password = config.password
                        factory.requestedHeartbeat = 30
                        factory.isAutomaticRecoveryEnabled = true

                        val connection = factory.newConnection()
                        val channel = connection!!.createChannel()

                        channel.exchangeDeclarePassive(config.exchange)
                        channel.queueDeclare(config.queue, false, false, true, null)
                        channel.queueBind(config.queue, config.exchange, routingKey)
                        return channel
                    }
                },
                object : ConsumerFactory {
                    override fun newConsumer(channel: Channel): Consumer {
                        return MeEventConsumer(channel, routingKey, queue)
                    }
                }
        ).start()
    }

    private fun startMessagesProcessing() {
        thread {
            while (true) {
                processMessage(queue.take())
            }
        }
    }

    private fun processMessage(messageWrapper: RmqMessageWrapper) {
        val deserializer = deserializerByRoutingKey[messageWrapper.routingKey]
        if (deserializer == null) {
            LOGGER.error("There is now deserializer for routingKey=${messageWrapper.routingKey}")
        }
        val event = deserializer!!.deserialize(messageWrapper.message)
        LOGGER.debug("Got event from rabbit mq: routingKey=${messageWrapper.routingKey}, class: ${event.message::class.java.name}, messageId=${event.messageId}")
        notifySubscribers(event)
    }
}