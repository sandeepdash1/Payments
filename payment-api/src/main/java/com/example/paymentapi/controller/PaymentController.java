
package com.example.paymentapi.controller;

import com.example.paymentapi.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService s) {
        this.service = s;
    }

    @PostMapping("/payments")
    public String create() {
        String id = UUID.randomUUID().toString();
        service.process(id);
        return "Accepted:" + id;
    }
}
