package com.example.paymentapi.service;

import com.example.paymentapi.model.*;
import com.example.paymentapi.repository.PaymentRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PaymentProcessor {
    private final PaymentRepository repository;
    private final BankClient bankClient;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private final Counter successCounter;
    private final Counter failureCounter;
    private final AtomicInteger processingGauge = new AtomicInteger();
    private final Timer processingTimer;

    public PaymentProcessor(PaymentRepository repository, BankClient bankClient,
                            KafkaTemplate<String, PaymentEvent> kafkaTemplate,
                            MeterRegistry meterRegistry) {
        this.repository = repository;
        this.bankClient = bankClient;
        this.kafkaTemplate = kafkaTemplate;
        this.successCounter = meterRegistry.counter("payments.processed", "status", "success");
        this.failureCounter = meterRegistry.counter("payments.processed", "status", "failed");
        meterRegistry.gauge("payments.processing", processingGauge);
        this.processingTimer = meterRegistry.timer("payments.processing.duration");
    }

    @Async("paymentExecutor")
    @Transactional
    public void process(String id) {
        processingGauge.incrementAndGet();
        Timer.Sample sample = Timer.start();
        try {
            Payment payment = repository.findById(id).orElseThrow();
            payment.setStatus(PaymentStatus.PROCESSING);
            try {
                BankClient.BankResponse response = bankClient.authorize(id, payment.getAmount());
                payment.setStatus(response.approved() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
            } catch (Exception ex) {
                payment.setStatus(PaymentStatus.FAILED);
            }
            repository.save(payment);
            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                successCounter.increment();
            } else {
                failureCounter.increment();
            }
            kafkaTemplate.send("payment-events", id,
                    new PaymentEvent(id, payment.getAmount(), payment.getStatus()));
        } finally {
            processingGauge.decrementAndGet();
            sample.stop(processingTimer);
        }
    }
}
