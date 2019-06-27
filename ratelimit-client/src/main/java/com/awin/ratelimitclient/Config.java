package com.awin.ratelimitclient;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.retry.annotation.EnableRetry;


@Configuration
@EnableRetry
public class Config {

    @Bean
    public RestOperations restOperations(RestTemplateBuilder builder) {
        return builder.build();
    }
}
