package com.lykke.me.test.client.config

data class MeTestClientConfig(val matchingEngineEndpoint: IpEndpointConfig,
                              val rabbitMqConfigs: Set<RabbitMqConfig>,
                              val testPrerequisitesConfig: TestPrerequisitesConfig,
                              val multiOrderStressTest: MultiOrderStressTestConfig)