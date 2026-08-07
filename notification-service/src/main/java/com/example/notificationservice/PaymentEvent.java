package com.example.notificationservice;

import java.math.BigDecimal;

public record PaymentEvent(String paymentId, BigDecimal amount, String status) {}
