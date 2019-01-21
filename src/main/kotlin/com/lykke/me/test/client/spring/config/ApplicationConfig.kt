package com.lykke.me.test.client.spring.config

import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.config.IpEndpointConfig
import com.lykke.me.test.client.config.MeTestClientConfig
import com.lykke.me.test.client.config.RabbitMqConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ApplicationConfig {

    @Bean
    open fun config(@Value("\${matching.engine.host}")
                    host: String,
                    @Value("\${matching.engine.port}")
                    port: Int): Config {
        // todo read from json
        return Config(MeTestClientConfig(IpEndpointConfig(host, port),
                setOf(RabbitMqConfig("amqp://lykke.history:lykke.history@127.0.0.1:5672",
                        "lykke.spot.matching.engine.out.events",
                        "lykke.spot.matching.engine.out.events.test.client.cashIn",
                        "1"))))
    }
}