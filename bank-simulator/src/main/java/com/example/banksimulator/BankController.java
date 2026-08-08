package com.example.banksimulator;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/bank")
public class BankController {
    @PostMapping("/authorize")
    public BankResponse authorize(@RequestBody BankRequest request) throws InterruptedException {
        long delay = ThreadLocalRandom.current().nextLong(200, 1001);
        Thread.sleep(delay);
        boolean approved = ThreadLocalRandom.current().nextInt(100) < 95;
        return new BankResponse(request.paymentId(), approved, approved ? "APPROVED" : "DECLINED");
    }

    public record BankRequest(String paymentId, BigDecimal amount) {
    }

    public record BankResponse(String paymentId, boolean approved, String message) {
    }
}
