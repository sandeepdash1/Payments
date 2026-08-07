package com.example.paymentapi.service;

import com.example.paymentapi.model.*;
import com.example.paymentapi.repository.PaymentRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentProcessor {
    private final PaymentRepository repository;
    private final BankClient bankClient;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public PaymentProcessor(PaymentRepository repository, BankClient bankClient,
                            KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.repository=repository; this.bankClient=bankClient; this.kafkaTemplate=kafkaTemplate;
    }

    @Async("paymentExecutor")
    @Transactional
    public void process(String id) {
        Payment payment = repository.findById(id).orElseThrow();
        payment.setStatus(PaymentStatus.PROCESSING);
        try {
            BankClient.BankResponse response = bankClient.authorize(id, payment.getAmount());
            payment.setStatus(response.approved() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        } catch (Exception ex) {
            payment.setStatus(PaymentStatus.FAILED);
        }
        repository.save(payment);
        kafkaTemplate.send("payment-events", id,
                new PaymentEvent(id, payment.getAmount(), payment.getStatus()));
    }
}
