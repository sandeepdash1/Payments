
package com.example.paymentapi.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Async("paymentExecutor")
    public void process(String id) {
        try {
            System.out.println(Thread.currentThread().getName() + " processing " + id);
            Thread.sleep((long) (Math.random() * 3000));
            if (Math.random() > 0.8) Thread.sleep(30000);
            System.out.println("Bank approved " + id);
            System.out.println("Publishing Kafka event (stub)");
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
