package com.example.paymentapi.service;

import com.example.paymentapi.model.Payment;
import com.example.paymentapi.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private final PaymentRepository repository;
    private final PaymentProcessor processor;

    public PaymentService(PaymentRepository repository, PaymentProcessor processor) {
        this.repository = repository;
        this.processor = processor;
    }

    public Payment create(java.math.BigDecimal amount) {
        Payment payment = repository.save(
                new Payment(java.util.UUID.randomUUID().toString(), amount));
        processor.process(payment.getId());
        return payment;
    }

    @Transactional(readOnly = true)
    public Payment get(String id) {
        return repository.findById(id).orElseThrow();
    }
}
