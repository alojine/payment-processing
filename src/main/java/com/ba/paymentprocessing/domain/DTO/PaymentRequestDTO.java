package com.ba.paymentprocessing.domain.DTO;

import java.math.BigDecimal;
import java.util.Currency;

public record PaymentRequestDTO(
        String paymentType,
        BigDecimal amount,
        Currency currency,
        String debtOrIban,
        String creditOrIban,
        String details,
        String bicCode
) {
}
