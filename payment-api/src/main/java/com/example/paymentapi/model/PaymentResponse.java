package com.example.paymentapi.model;

import java.math.BigDecimal;

public record PaymentResponse(String id, BigDecimal amount, PaymentStatus status) {}
