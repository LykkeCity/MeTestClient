package com.lykke.me.test.client.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
open class SwaggerConfig {
    @Bean
    open fun apiDocket(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(ApiInfo(
                        "ME test tool",
                        "ME test tool",
                        "1.0",
                        "",
                        null,
                        "",
                        "",
                        emptyList()
                ))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lykke.me.test.client.web.controllers"))
                .paths(PathSelectors.any())
                .build()

    }
}