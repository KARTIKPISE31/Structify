package com.structify.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraniteConfig {

    @Bean
    public WebClient graniteWebClient() {
        return WebClient.builder()
                .baseUrl("https://us-south.ml.cloud.ibm.com") // IBM Granite endpoint
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer YOUR_IBM_API_KEY")
                .build();
    }
}
