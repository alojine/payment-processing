package com.ba.paymentprocessing.controller;

import com.ba.paymentprocessing.domain.DTO.PaymentRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {
    @GetMapping
    public ResponseEntity<List<UUID>> getFilteredPayments(
            @RequestParam(required = false) Boolean areCanceled,
            @RequestParam(required = false) BigDecimal lowerBound,
            @RequestParam(required = false) BigDecimal upperBound
    ) {
        // return only Id's
        List<UUID> payments = new ArrayList<>();
        payments.add(UUID.fromString("h"));
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> getPaymentById(@PathVariable UUID id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> cancelPayment(@PathVariable UUID id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
