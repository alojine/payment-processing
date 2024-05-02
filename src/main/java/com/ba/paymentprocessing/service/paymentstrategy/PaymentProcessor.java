package com.ba.paymentprocessing.service.paymentstrategy;

import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.model.Payment;

import java.math.BigDecimal;

public interface PaymentProcessor {
    Payment validate(PaymentRequestDTO paymentRequestDTO);
    BigDecimal calculateCancellationFee(BigDecimal duration);
}
