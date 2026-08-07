package com.example.paymentapi.controller;

import com.example.paymentapi.model.*;
import com.example.paymentapi.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService service;
    public PaymentController(PaymentService service){this.service=service;}

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request){
        Payment payment=service.create(request.amount());
        return ResponseEntity.accepted().body(new PaymentResponse(payment.getId(),payment.getAmount(),payment.getStatus()));
    }

    @GetMapping("/{id}")
    public PaymentResponse get(@PathVariable String id){
        Payment p=service.get(id);
        return new PaymentResponse(p.getId(),p.getAmount(),p.getStatus());
    }
}
