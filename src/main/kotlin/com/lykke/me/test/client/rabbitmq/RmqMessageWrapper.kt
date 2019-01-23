package com.lykke.me.test.client.rabbitmq

class RmqMessageWrapper(val routingKey: String,
                        val message: ByteArray)