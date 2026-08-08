package com.example.notificationservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, PaymentEvent> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, PaymentEvent>();
        factory.setConsumerFactory(consumerFactory);

        // For this training application, skip a record that cannot be deserialized
        // after the ErrorHandlingDeserializer has captured the deserialization error.
        // Production systems would normally publish such records to a DLT instead.
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(0L, 0L)));
        return factory;
    }
}
