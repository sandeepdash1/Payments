package com.example.paymentapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpConfig {
    @Bean
    RestClient bankRestClient(RestClient.Builder builder, @Value("${bank.base-url:http://localhost:8081}") String bankBaseUrl) {
        return builder.baseUrl(bankBaseUrl).build();
    }
}
