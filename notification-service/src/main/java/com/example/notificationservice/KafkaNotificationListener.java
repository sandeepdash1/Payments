package com.example.notificationservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaNotificationListener {
    @KafkaListener(topics = "payment-events", groupId = "notification-service")
    public void onPaymentEvent(PaymentEvent event) {
        System.out.printf("Notification received: paymentId=%s status=%s amount=%s%n",
                event.paymentId(), event.status(), event.amount());
    }
}
