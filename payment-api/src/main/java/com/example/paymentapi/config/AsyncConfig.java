
package com.example.paymentapi.config;

import org.springframework.context.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean
    Executor paymentExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(10);
        ex.setMaxPoolSize(100);
        ex.setQueueCapacity(500);
        ex.setThreadNamePrefix("payment-");
        ex.initialize();
        return ex;
    }
}
