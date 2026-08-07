package com.example.paymentapi.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class BankClient {
    private final RestClient restClient;
    public BankClient(RestClient restClient){this.restClient=restClient;}
    public BankResponse authorize(String paymentId, BigDecimal amount){
        return restClient.post().uri("/bank/authorize")
                .body(new BankRequest(paymentId, amount))
                .retrieve().body(BankResponse.class);
    }
    public record BankRequest(String paymentId, BigDecimal amount){}
    public record BankResponse(String paymentId, boolean approved, String message){}
}
