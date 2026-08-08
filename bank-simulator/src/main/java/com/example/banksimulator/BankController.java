package com.example.banksimulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Value("${bank.simulator.delay-ms:0}")
    private long configuredDelayMs;

    @PostMapping("/authorize")
    public BankResponse authorize(@RequestBody BankRequest request) throws InterruptedException {
        long delay = configuredDelayMs > 0
                ? configuredDelayMs
                : ThreadLocalRandom.current().nextLong(200, 1001);

        Thread.sleep(delay);

        boolean approved = ThreadLocalRandom.current().nextInt(100) < 95;
        return new BankResponse(
                request.paymentId(),
                approved,
                approved ? "APPROVED" : "DECLINED"
        );
    }

    public record BankRequest(String paymentId, BigDecimal amount) {}
    public record BankResponse(String paymentId, boolean approved, String message) {}
}
