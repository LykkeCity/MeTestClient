package com.lykke.me.test.client.spring.config

import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.config.IpEndpointConfig
import com.lykke.me.test.client.config.MeTestClientConfig
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
        return Config(MeTestClientConfig(IpEndpointConfig(host, port)))
    }
}