package com.ba.paymentprocessing.dto;
import java.math.BigDecimal;

public record PaymentRequestDTO(
        String paymentType,
        BigDecimal amount,
        String currency,
        String debtOrIban,
        String creditOrIban,
        String details,
        String bicCode
) {
}
