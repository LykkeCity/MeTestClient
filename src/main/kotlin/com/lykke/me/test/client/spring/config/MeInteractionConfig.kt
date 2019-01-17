package com.lykke.me.test.client.spring.config

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilderImpl
import com.lykke.me.test.client.socket.MeProtoSocketClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MeInteractionConfig {

    @Bean
    open fun meClient(): MeClient {
        return MeProtoSocketClient()
    }

    @Bean
    open fun messageBuilder(): MessageBuilder {
        return MessageBuilderImpl()
    }
}