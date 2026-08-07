package com.example.paymentapi.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @Column(length = 36) private String id;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private PaymentStatus status;
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;
    protected Payment() {}
    public Payment(String id, BigDecimal amount) { this.id=id; this.amount=amount; this.status=PaymentStatus.CREATED; this.createdAt=Instant.now(); this.updatedAt=createdAt; }
    public String getId(){return id;} public BigDecimal getAmount(){return amount;} public PaymentStatus getStatus(){return status;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
    public void setStatus(PaymentStatus status){this.status=status; this.updatedAt=Instant.now();}
}
