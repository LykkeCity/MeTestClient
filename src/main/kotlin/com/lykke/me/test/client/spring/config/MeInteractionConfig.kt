package com.lykke.me.test.client.spring.config

import com.lykke.me.test.client.MeClient
import com.lykke.me.test.client.MeResponseListener
import com.lykke.me.test.client.config.Config
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilder
import com.lykke.me.test.client.outgoing.messages.utils.MessageBuilderImpl
import com.lykke.me.test.client.socket.MeProtoSocketClient
import com.lykke.me.test.client.socket.MeProtoSocketResponseListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MeInteractionConfig {

    @Bean(initMethod = "start")
    open fun meClient(meResponseListener: MeResponseListener,
                      config: Config): MeClient {
        return MeProtoSocketClient(meResponseListener as MeProtoSocketResponseListener,
                config.matchingEngineTestClient.matchingEngineEndpoint.host,
                config.matchingEngineTestClient.matchingEngineEndpoint.port,
                500,
                100)
    }

    @Bean
    open fun meResponseListener(): MeResponseListener {
        return MeProtoSocketResponseListener()
    }

    @Bean
    open fun messageBuilder(): MessageBuilder {
        return MessageBuilderImpl()
    }
}