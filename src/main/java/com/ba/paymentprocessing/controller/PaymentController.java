package com.ba.paymentprocessing.controller;

import com.ba.paymentprocessing.dto.PaymentResponseDTO;
import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<UUID>> getFilteredPayments(
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount
    ) {
        return new ResponseEntity<>(paymentService.getFilteredPayments(minAmount, maxAmount), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable UUID id) {
        return new ResponseEntity<>(paymentService.getPaymentById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        paymentService.createPayment(paymentRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> cancelPayment(@PathVariable UUID id) {
        paymentService.cancelPayment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
