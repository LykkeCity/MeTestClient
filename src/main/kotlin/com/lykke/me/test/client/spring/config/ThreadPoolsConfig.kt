package com.lykke.me.test.client.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
@EnableScheduling
@EnableAsync
open class ThreadPoolsConfig {

    @Bean
    open fun testRunnerThreadPool(): ThreadPoolTaskExecutor {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.setThreadNamePrefix( "test-runner-thread-")
        threadPoolTaskExecutor.setQueueCapacity(0)
        threadPoolTaskExecutor.corePoolSize = 0
        threadPoolTaskExecutor.maxPoolSize = Integer.MAX_VALUE
        threadPoolTaskExecutor.keepAliveSeconds = 60

        return threadPoolTaskExecutor
    }
}