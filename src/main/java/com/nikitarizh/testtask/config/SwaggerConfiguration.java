package com.nikitarizh.testtask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nikitarizh.testtask"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }


    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "Codex testtask",
                "Codex testtask",
                "1.0",
                "Terms of service",
                new Contact("Nikita Rizhkousky", "https://github.com/nikitarizh", "nrigworks@gmail.com"),
                "",
                "",
                new ArrayList<>());
        return apiInfo;
    }
}