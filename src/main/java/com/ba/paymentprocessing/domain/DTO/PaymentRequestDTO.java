package com.ba.paymentprocessing.domain.DTO;

import com.ba.paymentprocessing.type.PaymentType;

import java.math.BigDecimal;
import java.util.Currency;

public record PaymentRequestDTO(
        PaymentType paymentType,
        BigDecimal amount,
        Currency currency,
        String debtOrIban,
        String creditOrIban,
        String details,
        String bicCode
) {
}
