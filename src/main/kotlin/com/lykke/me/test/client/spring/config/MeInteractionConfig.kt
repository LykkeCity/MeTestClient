package com.lykke.me.test.client.spring.config

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.MeListener
import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.incoming.events.MeEvent
import com.lykke.me.test.client.incoming.response.Response
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilderImpl
import com.lykke.me.test.client.rabbitmq.MeRabbitMqProtoEventListener
import com.lykke.me.test.client.socket.MeSocketProtoClient
import com.lykke.me.test.client.socket.MeSocketProtoResponseListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MeInteractionConfig {

    @Bean(initMethod = "start")
    open fun meClient(meResponseListener: MeListener<Response>,
                      config: Config): MeClient {
        return MeSocketProtoClient(meResponseListener as MeSocketProtoResponseListener,
                config.matchingEngineTestClient.matchingEngineEndpoint.host,
                config.matchingEngineTestClient.matchingEngineEndpoint.port)
    }

    @Bean
    open fun meResponseListener(): MeListener<Response> {
        return MeSocketProtoResponseListener()
    }

    @Bean(initMethod = "start")
    open fun meEventListener(config: Config): MeListener<MeEvent> {
        return MeRabbitMqProtoEventListener(config.matchingEngineTestClient.rabbitMqConfigs) as MeListener<MeEvent>
    }

    @Bean
    open fun messageBuilder(): MessageBuilder {
        return MessageBuilderImpl()
    }
}