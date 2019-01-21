package com.lykke.me.test.client.spring.config

import com.lykke.me.test.client.config.Config
import com.lykke.utils.config.ConfigInitializer
import org.apache.log4j.Logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
open class ApplicationConfig {

    companion object {
        private val LOGGER = Logger.getLogger(ApplicationConfig::class.java.name)
    }

    @Bean
    open fun config(environment: Environment): Config {
        return if (environment.acceptsProfiles("local_config")) {
            ConfigInitializer.initConfig("local", classOfT = Config::class.java)
        } else {
            val commandLineArgs = environment.getProperty("nonOptionArgs", Array<String>::class.java)
            if (commandLineArgs == null) {
                val errorMessage = "Not enough args. Usage: httpConfigString"
                LOGGER.error(errorMessage)
                throw IllegalArgumentException(errorMessage)
            }
            ConfigInitializer.initConfig(commandLineArgs[0], classOfT = Config::class.java)
        }
    }

}