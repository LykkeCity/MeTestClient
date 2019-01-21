package com.lykke.me.test.client.config

data class RabbitMqConfig(val uri: String,
                          val exchange: String,
                          val queue: String,
                          val routingKey: String)