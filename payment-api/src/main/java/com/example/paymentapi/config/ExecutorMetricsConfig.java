package com.example.paymentapi.config;

import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ExecutorMetricsConfig {
    @Bean
    MeterBinder paymentExecutorMetrics(@Qualifier("paymentExecutor") Executor executor) {
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) executor;
        return registry -> new ExecutorServiceMetrics(
                taskExecutor.getThreadPoolExecutor(),
                "payment.executor",
                java.util.Collections.emptyList()).bindTo(registry);
    }
}
