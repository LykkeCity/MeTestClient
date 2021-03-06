package com.lykke.me.test.client.config

import com.lykke.me.subscriber.config.RabbitMqConfig

data class MeTestClientConfig(val matchingEngineEndpoint: IpEndpointConfig,
                              val rabbitMqConfigs: Set<RabbitMqConfig>,
                              val testPrerequisitesConfig: TestPrerequisitesConfig,
                              val limitOrderTestConfig: LimitOrderTestConfig,
                              val multiOrderStressTest: MultiOrderStressTestConfig,
                              val marketOrderTestConfig: MarketOrderTestConfig)