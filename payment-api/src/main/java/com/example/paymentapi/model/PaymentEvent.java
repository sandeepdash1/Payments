package com.example.paymentapi.model;

import java.math.BigDecimal;

public record PaymentEvent(String paymentId, BigDecimal amount, PaymentStatus status) {}
